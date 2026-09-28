package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.AdvancementManager;
import net.minecraft.server.FunctionManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.WorldData;
import net.minecraft.world.gen.loot.LootTables;
import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.executors.api.MainThreadExecutor;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.api.server.MinecraftServerExtension;
import net.ornithemc.osl.resource.loader.api.server.resource.reload.DataReloaders;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.ServerResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.DirectoryPackSource;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MainThreadExecutor, MinecraftServerExtension {

	private static final boolean LOOT_TABLE_MANAGER_EXISTS = MinecraftVersion.resolve().compareTo("15w43a") >= 0;
	private static final boolean FUNCTION_MANAGER_EXISTS = MinecraftVersion.resolve().compareTo("1.12-pre1") >= 0;
	private static final boolean ADVANCEMENT_MANAGER_EXISTS = MinecraftVersion.resolve().compareTo("17w13a") >= 0;
	private static final boolean STRUCTURE_MANAGER_EXISTS = MinecraftVersion.resolve().compareTo("15w31a") >= 0;
	private static final boolean PLAYER_MANAGER_IS_RELOADER = MinecraftVersion.resolve().compareTo("17w15a") >= 0;

	@Shadow
	private PlayerManager playerManager;
	@Shadow
	protected ServerWorld[] worlds;

	@Unique
	private SimpleResourcePackRepository packRepository;
	@Unique
	private SimpleReloadableResourceManager resourceManager;

	@Conditional(minecraftVersion = @Version(">=15w43a"))
	@Unique
	private LootTables lootTables;
	@Conditional(minecraftVersion = @Version(">=1.12-pre1"))
	@Unique
	private FunctionManager functions;
	@Conditional(minecraftVersion = @Version(">=17w13a"))
	@Unique
	private AdvancementManager advancements;

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
	private void osl$resource_loader$loadDataPacks(CallbackInfo ci, @Local WorldStorage storage, @Local WorldData data) {
		this.onLoadWorldBeforeCreateServerWorlds(storage, data);
	}

	@Conditional(minecraftVersion = @Version(">=17w18a"))
	@Redirect(
		method = "reloadResources",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/loot/LootTables;reload()V"
		)
	)
	private void osl$resource_loader$cancelLootTablesReload(LootTables lootTables) {
		// the loot table manager is added as a resource reload listener
	}

	@Conditional(minecraftVersion = @Version(">=17w18a"))
	@Redirect(
		method = "reloadResources",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/AdvancementManager;reload()V"
		)
	)
	private void osl$resource_loader$cancelAdvancementsReload(AdvancementManager advancements) {
		// the advancement manager is added as a resource reload listener
	}

	@Conditional(minecraftVersion = @Version(">=1.12-pre1"))
	@Redirect(
		method = "reloadResources",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/FunctionManager;reload()V"
		)
	)
	private void osl$resource_loader$cancelFunctionsReload(FunctionManager functions) {
		// the function manager is added as a resource reload listener
	}

	@Conditional(minecraftVersion = @Version(">=17w18a"))
	@Inject(
		method = "reloadResources",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/PlayerManager;reloadResources()V"
		)
	)
	private void osl$resource_loader$reloadResources(CallbackInfo ci) {
		this.reloadDataPacks(this.worlds[0].getData());
	}

	@Unique
	protected void onLoadWorldBeforeCreateServerWorlds(WorldStorage storage, WorldData data) {
		File dataDir = new File(storage.getDirectory(), "data");

		if (LOOT_TABLE_MANAGER_EXISTS) {
			this.lootTables = new LootTables(new File(dataDir, "loot_tables"));
		}
		if (FUNCTION_MANAGER_EXISTS) {
			this.functions = new FunctionManager(new File(dataDir, "functions"), (MinecraftServer) (Object) this);
		}
		if (ADVANCEMENT_MANAGER_EXISTS) {
			this.advancements = new AdvancementManager(new File(dataDir, "advancements"));
		}

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

		// Vanilla's data reloaders all trigger a reload in <init>
		// we cancel those since we do a full reload here anyway
//		this.resourceManager.registerReloader(DataReloaders.CRAFTING_MANAGER, new RecipesManager()); // TODO
		if (LOOT_TABLE_MANAGER_EXISTS) {
			this.resourceManager.registerReloader(DataReloaders.LOOT_TABLE_MANAGER, (ResourceReloader) this.lootTables);
		}
		if (FUNCTION_MANAGER_EXISTS) {
			this.resourceManager.registerReloader(DataReloaders.FUNCTION_MANAGER, (ResourceReloader) this.functions);
		}
		if (ADVANCEMENT_MANAGER_EXISTS) {
			this.resourceManager.registerReloader(DataReloaders.ADVANCEMENT_MANAGER, (ResourceReloader) this.advancements);
		}
		if (STRUCTURE_MANAGER_EXISTS) {
			this.resourceManager.registerReloader(DataReloaders.STRUCTURE_MANAGER, (ResourceReloader) storage.getStructureManager());
		}

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

		data.getEnabledDataPacks().clear();
		data.getDisabledDataPacks().clear();

		for (ResourcePackSummary pack : this.packRepository.getUnselectedPacks()) {
			data.getDisabledDataPacks().add(pack.getId());
		}
		for (ResourcePackSummary pack : this.packRepository.getSelectedPacks()) {
			data.getEnabledDataPacks().add(pack.getId());
		}
	}

	@Conditional(minecraftVersion = @Version(">=15w43a"))
	@Override
	public LootTables getLootTables() {
		return this.lootTables;
	}

	@Conditional(minecraftVersion = @Version(">=1.12-pre1"))
	@Overwrite
	public FunctionManager getFunctions() {
		return this.functions;
	}

	@Conditional(minecraftVersion = @Version(">=17w13a"))
	@Overwrite
	public AdvancementManager getAdvancements() {
		return this.advancements;
	}

	@Conditional(minecraftVersion = @Version("<17w18a"))
	@Override
	public void reloadResources() {
		if (this.isRunningOnSameThread()) {
			this.reloadDataPacks(this.worlds[0].getData());
			if (PLAYER_MANAGER_IS_RELOADER) {
				this.playerManager.reloadResources();
			}
		} else {
			this.execute(this::reloadResources);
		}
	}
}
