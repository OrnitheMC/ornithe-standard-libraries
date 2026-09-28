package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.WorldData;
import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.osl.executors.api.MainThreadExecutor;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.api.server.MinecraftServerExtension;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.ServerResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.DirectoryPackSource;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MainThreadExecutor, MinecraftServerExtension {

	@Shadow
	protected ServerWorld[] worlds;

	@Unique
	private SimpleResourcePackRepository packRepository;
	@Unique
	private SimpleReloadableResourceManager resourceManager;


	@Inject(
		method = "<init>*",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$initDataPacks(CallbackInfo ci) {
		this.packRepository = SimpleResourcePackRepository.server();
		this.resourceManager = SimpleReloadableResourceManager.server();
	}

	@Inject(
		method = "loadWorld",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/storage/WorldStorage;loadData()Lnet/minecraft/world/WorldData;",
			shift = Shift.BY,
			by = 2
		)
	)
	private void osl$resource_loader$storeWorldData(CallbackInfo ci, @Local WorldData data, @Share("osl$resource_loader$worldData") LocalRef<WorldData> dataRef) {
		dataRef.set(data);
	}

	@Inject(
		method = "loadWorld",
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/world/storage/WorldStorage;loadData()Lnet/minecraft/world/WorldData;"
			)
		),
		at = @At(
			// inject before the for-loop for ServerWorld instancing
			value = "FIELD",
			target = "Lnet/minecraft/server/MinecraftServer;worlds:[Lnet/minecraft/server/world/ServerWorld;",
			ordinal = 0,
			shift = Shift.BY,
			// it's extreme but this ensures that the injector is only invoked once
			by = -5
		)
	)
	private void osl$resource_loader$loadDataPacks(CallbackInfo ci, @Local WorldStorage storage, @Share("osl$resource_loader$worldData") LocalRef<WorldData> dataRef) {
		this.onLoadWorldBeforeCreateServerWorlds(storage, dataRef.get());
	}

	@Unique
	protected void onLoadWorldBeforeCreateServerWorlds(WorldStorage storage, WorldData data) {
		this.loadDataPacks(storage.getDirectory().toPath(), storage, data);
	}

	@Unique
	private void loadDataPacks(Path dir, WorldStorage storage, WorldData data) {
		this.packRepository.reset();
		this.packRepository.setCallbacks(null, null);

		this.packRepository.addSource(new ServerResourcePacks());
		this.packRepository.addSource(BundledModResourcePacks.server());
		this.packRepository.addSource(new DirectoryPackSource(dir.resolve("datapacks")));

		this.packRepository.init();
		this.packRepository.reload();

		List<String> packsToSelect = new ArrayList<>();

		for (String id : data.getEnabledDataPacks()) {
			ResourcePackSummary pack = this.packRepository.getPack(id);

			if (pack == null) {
				ResourceLoader.LOGGER.warn("Missing data pack {}", id);
			} else {
				packsToSelect.add(id);
			}
		}

		this.packRepository.setSelectedPacks(packsToSelect);

		this.resourceManager.reset();
		this.resourceManager.addAll(this.packRepository.openSelectedPacks());

		this.resourceManager.init();
//		this.resourceManager.partialReload(); // unnecessary since full reload follows

		this.reloadDataPacks(data);
	}

	@Unique
	protected void reloadDataPacks(WorldData data) {
		List<ResourcePackSummary> packsToSelect = new ArrayList<>(this.packRepository.getSelectedPacks());

		for (ResourcePackSummary pack : this.packRepository.getUnselectedPacks()) {
			if (!data.getDisabledDataPacks().contains(pack.getId())) {
				ResourceLoader.LOGGER.info("Found new data pack {}, loading it automatically", pack.getId());

				pack.getDefaultPosition().insert(packsToSelect, pack, false);
			}
		}

		this.packRepository.setSelectedPacks(packsToSelect.stream().map(ResourcePackSummary::getId).collect(Collectors.toList()));		

		try {
			this.resourceManager.reload(this.packRepository.openSelectedPacks());
		} catch (Throwable t) {
			ResourceLoader.LOGGER.error("Failed to reload data packs", t);
		}

		this.saveDataPacks(data);
	}

	@Unique
	protected void saveDataPacks() {
		this.saveDataPacks(this.worlds[0].getData());
	}

	@Unique
	private void saveDataPacks(WorldData data) {
		data.getEnabledDataPacks().clear();
		data.getDisabledDataPacks().clear();

		for (ResourcePackSummary pack : this.packRepository.getUnselectedPacks()) {
			data.getDisabledDataPacks().add(pack.getId());
		}
		for (ResourcePackSummary pack : this.packRepository.getSelectedPacks()) {
			data.getEnabledDataPacks().add(pack.getId());
		}
	}

	@Override
	public void reloadResources() {
		if (this.isRunningOnSameThread()) {
			this.reloadDataPacks(this.worlds[0].getData());
		} else {
			this.execute(this::reloadResources);
		}
	}
}
