package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(
		method = "reloadResources",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$resource_loader$startResourceReload(CallbackInfo ci) {
		ClientResourceLoaderEvents.START_RESOURCE_RELOAD.invoker().run();
	}

	@Inject(
		method = "reloadResources",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$endResourceReload(CallbackInfo ci) {
		ClientResourceLoaderEvents.END_RESOURCE_RELOAD.invoker().run();
	}
}
