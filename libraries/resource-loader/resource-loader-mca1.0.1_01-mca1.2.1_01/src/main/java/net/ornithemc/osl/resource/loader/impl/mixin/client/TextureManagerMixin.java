package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.IOException;
import java.io.InputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.texture.TextureManager;

import net.ornithemc.osl.lifecycle.api.client.MinecraftInstance;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;

@Mixin(TextureManager.class)
public class TextureManagerMixin implements ResourceReloadListener {

	@Unique
	private Minecraft minecraft;
	@Unique
	private ResourceManager resourceManager;

	@Unique
	private boolean vanillaReloading;
	@Unique
	private boolean reloading;

	@Shadow
	private void reload() { }

	@Redirect(
		method = "load(Ljava/lang/String;)I",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Class;getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;"
		)
	)
	private InputStream osl$resource_loader$loadResource(Class<TextureManager> cls, String path) {
		try {
			return this.resourceManager.getResource(path);
		} catch (IOException e) {
			return null;
		}
	}

	@Redirect(
		method = "reload",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Class;getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;"
		)
	)
	private InputStream osl$resource_loader$reloadResource(Class<TextureManager> cls, String path) {
		try {
			return this.resourceManager.getResource(path);
		} catch (IOException e) {
			return null;
		}
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$initResourceManager(CallbackInfo ci) {
		this.minecraft = MinecraftInstance.get();
		this.resourceManager = ResourceManager.client();
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
				this.minecraft.reloadResources();

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
