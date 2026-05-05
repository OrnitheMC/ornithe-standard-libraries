package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resource.pack.TexturePack;
import net.minecraft.client.resource.pack.TexturePacks;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.impl.ClientResourcePacks;
import net.ornithemc.osl.resource.loader.impl.access.TexturePacksAccess;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourceManagerAdapter;
import net.ornithemc.osl.resource.loader.impl.adapter.WrappedTexturePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(TexturePacks.class)
public class TexturePacksMixin implements TexturePacksAccess, ResourcePackRepository.Source {

	@Shadow
	private List<TexturePack> availablePacks;
	@Shadow
	private Map<String, TexturePack> availablePacksByKey;
	@Shadow
	private TexturePack defaultPack;
	@Shadow
	private TexturePack selected;

	@Unique
	private SimpleResourcePackRepository resourcePacks;
	@Unique
	private TexturePack resourceManager;
	@Unique
	private Map<String, TexturePack> availablePacksById;

	@Unique
	private TexturePack actuallySelected;
	@Unique
	private boolean vanillaReloading;
	@Unique
	private boolean reloading;

	@Shadow
	private void reload() { }

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/pack/TexturePacks;reload()V"
		)
	)
	private void osl$resource_loader$initResourcePackRepository(CallbackInfo ci) {
		this.resourcePacks = SimpleResourcePackRepository.client();
		// wrap the ResourceManager into a texture pack, and set this as selected
		// this way we don't have to redirect every call to TexturePack::getResource!
		this.resourceManager = new ResourceManagerAdapter(ResourceManager.client());
		this.availablePacksById = new HashMap<>();

		this.resourcePacks.setCallbacks(null, this::selectionChanged);

		this.resourcePacks.addSource(new ClientResourcePacks(this.defaultPack));
		this.resourcePacks.addSource(new BundledModResourcePacks());
		this.resourcePacks.addSource(this); // texturepacks/ directory source

		this.resourcePacks.init();
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/pack/TexturePack;open()V"
		)
	)
	private void osl$resource_loader$initSelected(CallbackInfo ci) {
		if (!this.availablePacks.contains(this.actuallySelected)) {
			this.selectPack(null);
		}
	}

	@Inject(
		method = "select",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$resource_loader$select(TexturePack pack, CallbackInfoReturnable<Boolean> cir) {
		if (pack == this.actuallySelected) {
			cir.setReturnValue(false);
		}
	}

	@Inject(
		method = "select",
		at = @At(
			value = "RETURN",
			ordinal = 1 // only on texture pack change
		)
	)
	private void osl$resource_loader$selected(TexturePack pack, CallbackInfoReturnable<Boolean> cir) {
		this.selectPack(this.selected);
	}

	@Inject(
		method = "reload",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$resource_loader$startReload(CallbackInfo ci) {
		if (!this.vanillaReloading) {
			this.vanillaReloading = true;

			if (!this.reloading) {
				this.resourcePacks.reload();

				// cancel this call as the pack repository will trigger
				// another recursive call through this.loadResourcePacks
				ci.cancel();
			}
		}
	}

	@Inject(
		method = "reload",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$endReload(CallbackInfo ci) {
		this.vanillaReloading = false;

		if (this.actuallySelected != this.resourceManager) {
			this.actuallySelected = this.selected;
			this.selected = this.resourceManager;
		}
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		this.reloading = true;
		this.reload();
		this.reloading = false;

		this.availablePacksById.clear();

		for (TexturePack pack : this.availablePacks) {
			if (pack == this.defaultPack) {
				continue;
			}

			ResourcePack resourcePack = new WrappedTexturePack(pack);
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

	@Override
	public TexturePack osl$resource_loader$getActuallySelected() {
		return this.actuallySelected;
	}

	@Unique
	private void selectPack(TexturePack pack) {
		if (pack == null || pack == this.defaultPack) {
			this.resourcePacks.setSelectedPacks(Collections.emptyList());
		} else {
			this.resourcePacks.setSelectedPacks(Collections.singletonList(WrappedTexturePack.getId(pack)));
		}

		this.actuallySelected = pack;
		this.selected = this.resourceManager;
	}

	@Unique
	private void selectionChanged() {
		this.actuallySelected = this.defaultPack;

		for (ResourcePackSummary summary : this.resourcePacks.getSelectedPacks()) {
			TexturePack pack = this.availablePacksById.get(summary.getId());

			if (pack != null) {
				this.actuallySelected = pack;
			}
		}
	}
}
