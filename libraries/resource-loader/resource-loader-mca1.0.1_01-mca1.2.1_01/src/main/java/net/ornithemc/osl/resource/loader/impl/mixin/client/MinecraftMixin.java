package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.client.render.world.WorldRenderer;

import net.ornithemc.osl.resource.loader.api.client.MinecraftExtension;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.client.resource.reload.AssetsReloaders;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.impl.ClientResourcePacks;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.TexturePack;
import net.ornithemc.osl.resource.loader.impl.TexturePackDetector;
import net.ornithemc.osl.resource.loader.impl.access.GameOptionsAccess;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.DirectoryPackSource;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(Minecraft.class)
public class MinecraftMixin implements MinecraftExtension {

	@Shadow
	private File gameDir;
	@Shadow
	private GameOptions options;
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
			target = "net/minecraft/client/render/texture/TextureManager"
		)
	)
	private void osl$resource_loader$initPackRepository(CallbackInfo ci) {
		Path dir = this.gameDir.toPath();

		this.packRepository = SimpleResourcePackRepository.client();

		this.packRepository.reset();
		this.packRepository.setCallbacks(null, null);

		this.packRepository.addSource(new ClientResourcePacks());
		this.packRepository.addSource(new BundledModResourcePacks());
		this.packRepository.addSource(new DirectoryPackSource(dir.resolve("texturepacks"), new TexturePackDetector()));

		this.packRepository.init();
		this.packRepository.reload();

		String skin = ((GameOptionsAccess) this.options).osl$resource_loader$getSkin();

		for (ResourcePackSummary summary : this.packRepository.getAvailablePacks()) {
			ResourcePack pack = summary.open();

			if (pack instanceof TexturePack && pack.getName().equals(skin)) {
				this.packRepository.setSelectedPacks(Collections.singleton(summary.getId()));
			}
		}
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
