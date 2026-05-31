package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.texture.TextureManager;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(TextureManager.class)
public class TextureManagerMixin implements ResourceReloadListener {

	@Unique
	private SimpleResourcePackRepository packRepository;
	@Unique
	private SimpleReloadableResourceManager resourceManager;

	@Unique
	private boolean vanillaReloading;
	@Unique
	private boolean reloading;

	@Shadow
	private void reload() { }

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/render/texture/TextureManager;texturePacks:Lnet/minecraft/client/resource/pack/TexturePacks;",
			ordinal = 0
		)
	)
	private void osl$resource_loader$initResourceManager(CallbackInfo ci) {
		this.packRepository = SimpleResourcePackRepository.client();
		this.resourceManager = SimpleReloadableResourceManager.client();

		this.resourceManager.reset();
		// ensure the selected packs have been applied
		this.resourceManager.reload(this.packRepository.openSelectedPacks());

		// let this class act as the texture reloader
		this.resourceManager.addReloader(this);
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
				this.resourceManager.reload(this.packRepository.openSelectedPacks());

				// cancel this call as the resource manager will trigger
				// another recursive call through this.resourcesReloaded
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
	}

	@Override
	public void resourcesReloaded(ResourceManager manager) {
		this.reloading = true;
		this.reload();
		this.reloading = false;
	}
}
