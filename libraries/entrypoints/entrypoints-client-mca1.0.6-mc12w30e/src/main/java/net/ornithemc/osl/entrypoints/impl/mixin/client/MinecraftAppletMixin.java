package net.ornithemc.osl.entrypoints.impl.mixin.client;

import java.applet.Applet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;

import net.minecraft.client.MinecraftApplet;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.impl.LaunchUtils;

@SuppressWarnings("serial")
@Mixin(MinecraftApplet.class)
public class MinecraftAppletMixin extends Applet {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$entrypoints$init(CallbackInfo ci) {
		EntrypointUtils.invoke(
			ClientModInitializer.ENTRYPOINT_KEY,
			ClientModInitializer.class,
			ClientModInitializer::initClient
		);
		EntrypointUtils.invoke(
			ModInitializer.ENTRYPOINT_KEY,
			ModInitializer.class,
			ModInitializer::init
		);

		ClientLaunchEvents.PARSE_RUN_ARGS.invoker().accept(LaunchUtils.wrapFabricRunArgs(this::getParameter));
	}
}
