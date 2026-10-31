package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.client.render.world.WorldRenderer;

import net.ornithemc.osl.resource.loader.api.client.MinecraftExtension;
import net.ornithemc.osl.resource.loader.api.client.resource.reload.AssetsReloaders;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(Minecraft.class)
public class MinecraftMixin implements MinecraftExtension {

	@Shadow
	private TextureManager textureManager;
	@Shadow
	private WorldRenderer worldRenderer;

	@Unique
	private SimpleResourcePackRepository packRepository;
	@Unique
	private SimpleReloadableResourceManager resourceManager;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$modifyCrashReports(CallbackInfo ci) {
		Minecraft minecraft = (Minecraft) (Object) this;

		minecraft.addCrashListener((report, section) -> {
			section.addDetail("Resource Packs", () -> {
				return this.packRepository.getSelectedPacks().stream()
					.map(summary -> {
						String s = summary.getId();
						if (!summary.getCompatibility().isCompatible()) {
							s += " (incompatible)";
						}

						return s;
					})
					.collect(Collectors.joining(", "));
			});
		});
	}

	@Inject(
		method = "init",
		at = @At(
			value = "NEW",
			target = "net/minecraft/client/resource/pack/TexturePacks"
		)
	)
	private void osl$resource_loader$initPackRepository(CallbackInfo ci) {
		this.packRepository = SimpleResourcePackRepository.client();
	}

	@Inject(
		method = "init",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/Minecraft;textureManager:Lnet/minecraft/client/render/texture/TextureManager;",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$initResourceManager(CallbackInfo ci) {
		this.resourceManager = SimpleReloadableResourceManager.client();

		this.resourceManager.reset();
		this.resourceManager.addAll(this.packRepository.openSelectedPacks());

		this.reloadResources();

		this.resourceManager.registerReloadedReloader(AssetsReloaders.TEXTURE_MANAGER, (ResourceReloader) this.textureManager);

		this.resourceManager.init();
		// Vanilla reloaders are triggered when registered
		// OSL reloaders are queued and reloaded at once
		this.resourceManager.partialReload();
	}

	@Override
	public void reloadResources() {
		this.packRepository.reload();

		try { 
			this.resourceManager.reload(this.packRepository.openSelectedPacks());
		} catch (RuntimeException e) {
			ResourceLoader.LOGGER.warn("Error while reloading resources", e);
		}

		if (this.worldRenderer != null) {
			this.worldRenderer.reload();
		}
	}
}
