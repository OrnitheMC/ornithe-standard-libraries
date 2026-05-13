package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resource.pack.ResourcePacks;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.impl.ClientResourcePacks;
import net.ornithemc.osl.resource.loader.impl.adapter.WrappedResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(ResourcePacks.class)
public class ResourcePacksMixin implements ResourcePackRepository.Source {

	@Shadow @Final
	private List<ResourcePacks.Entry> availablePacks;
	@Shadow @Final
	private List<ResourcePacks.Entry> appliedPacks;

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
		List<String> selection = new ArrayList<>();

		for (ResourcePacks.Entry pack : this.appliedPacks) {
			selection.add(WrappedResourcePack.getId(pack.get()));
		}

		this.packRepository.setSelectedPacks(selection);
	}

	@Unique
	private void selectionChanged() {
		this.appliedPacks.clear();

		for (ResourcePackSummary summary : this.packRepository.getSelectedPacks()) {
			ResourcePacks.Entry pack = this.availablePacksById.get(summary.getId());

			if (pack != null) {
				this.appliedPacks.add(pack);
			}
		}
	}
}
