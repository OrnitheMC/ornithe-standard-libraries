package net.ornithemc.osl.entrypoints.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(
		method = "main",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$entrypoints$init(CallbackInfo ci) {
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
	}

	@Inject(
		method = "main",
		at = @At(
			value = "INVOKE",
			target = "Ljava/awt/Window;addWindowListener(Ljava/awt/event/WindowListener;)V"
		)
	)
	private static void osl$entrypoints$parseRunArgs(String[] args, CallbackInfo ci) {
		ClientLaunchEvents.PARSE_RUN_ARGS.invoker().accept(args);
	}
}
