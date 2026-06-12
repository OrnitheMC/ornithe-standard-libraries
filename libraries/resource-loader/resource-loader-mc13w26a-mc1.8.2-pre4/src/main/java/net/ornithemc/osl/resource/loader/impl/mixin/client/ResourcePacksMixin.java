package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resource.pack.CustomResourcePack;
import net.minecraft.client.resource.pack.ResourcePacks;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ClientPackSource;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.impl.ClientResourcePacks;
import net.ornithemc.osl.resource.loader.impl.adapter.WrappedResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.DirectoryPackSource;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(ResourcePacks.class)
public class ResourcePacksMixin implements ResourcePackRepository.Source {

	@Shadow @Final
	private List<ResourcePacks.Entry> availablePacks;
	@Shadow @Final
	private List<ResourcePacks.Entry> appliedPacks;
	@Shadow @Final
	private net.minecraft.client.resource.pack.ResourcePack defaultPack;

	@Unique
	private SimpleResourcePackRepository packRepository;
	@Unique
	private Map<String, ResourcePacks.Entry> availablePacksById;

	@Unique
	private boolean vanillaReloading;
	@Unique
	private boolean reloading;

	@Shadow
	private void load() { }

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/pack/ResourcePacks;load()V"
		)
	)
	private void osl$resource_loader$initAvailable(CallbackInfo ci) {
		this.packRepository = SimpleResourcePackRepository.client();
		this.availablePacksById = new HashMap<>();

		this.packRepository.reset();
		this.packRepository.setCallbacks(null, this::selectionChanged);

		this.packRepository.addSource(new ClientResourcePacks((ResourcePacks) (Object) this));
		this.packRepository.addSource(new BundledModResourcePacks());
		this.packRepository.addSource(this); // resourcepacks/ directory source

		this.packRepository.init();
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$initSelection(CallbackInfo ci) {
		this.updateSelection();
	}

	@Inject(
		method = "load",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$resource_loader$startReload(CallbackInfo ci) {
		if (!this.vanillaReloading) {
			this.vanillaReloading = true;

			if (!this.reloading) {
				this.packRepository.reload();

				// cancel this call as the pack repository will trigger
				// another recursive call through this.loadResourcePacks
				ci.cancel();
			}
		}
	}

	@Inject(
		method = "load",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$endReload(CallbackInfo ci) {
		this.vanillaReloading = false;
	}

	@Inject(
		method = "apply",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$updateSelection(CallbackInfo ci) {
		this.updateSelection();
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		this.reloading = true;
		this.load();
		this.reloading = false;

		this.availablePacksById.clear();

		for (ResourcePacks.Entry pack : this.availablePacks) {
			ResourcePack resourcePack = new WrappedResourcePack(pack.get());
			ResourcePackSummary summary = ResourcePackSummary.create(
				resourcePack,
				this.packId(pack),
				false,
				false,
				PackPosition.TOP
			);

			this.availablePacksById.put(summary.getId(), pack);

			if (summary != null) {
				consumer.accept(summary);
			}
		}
	}

	@Unique
	private void updateSelection() {
		this.packRepository.setSelectedPacks(this.appliedPacks.stream().map(this::packId).collect(Collectors.toList()));
	}

	@Unique
	private void selectionChanged() {
		this.appliedPacks.clear();

		for (ResourcePackSummary summary : this.packRepository.getSelectedPacks()) {
			ResourcePacks.Entry pack = this.availablePacksById.get(summary.getId());

			// vanilla and server packs are handled separately
			if (pack != null) {
				this.appliedPacks.add(pack);
			}
		}
	}

	@Unique
	private String packId(ResourcePacks.Entry unopenedPack) {
		net.minecraft.client.resource.pack.ResourcePack pack = unopenedPack.get();

		if (pack == this.defaultPack) {
			return ClientPackSource.DEFAULT_PACK_ID;
		} else if (ClientResourcePacks.NEW_RESOURCE_PACKS_GUI && pack == ((ResourcePacks)(Object)this).getServerPack() /* @Shadow field/method would fail in 1.6! */) {
			return ClientPackSource.SERVER_PACK_ID;
		} else if (pack instanceof CustomResourcePack) {
			return DirectoryPackSource.packId(((CustomResourcePackAccess) pack).accessFile().toPath());
		} else {
			return "resourcepack/" + pack.getName();
		}
	}
}
