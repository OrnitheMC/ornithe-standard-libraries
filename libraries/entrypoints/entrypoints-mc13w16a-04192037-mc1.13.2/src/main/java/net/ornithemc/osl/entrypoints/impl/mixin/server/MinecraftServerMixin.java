package net.ornithemc.osl.entrypoints.impl.mixin.server;

import org.quiltmc.loader.api.entrypoint.EntrypointUtil;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerLaunchEvents;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject(
		method = "main",
		remap = false,
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$entrypoints$init(CallbackInfo ci) {
		EntrypointUtil.invoke(
			ServerModInitializer.ENTRYPOINT_KEY,
			ServerModInitializer.class,
			ServerModInitializer::initServer
		);
		EntrypointUtil.invoke(
			ModInitializer.ENTRYPOINT_KEY,
			ModInitializer.class,
			ModInitializer::init
		);
	}

	@Inject(
		method = "main",
		remap = false,
		at = @At(
			value = "NEW",
			target = "Lnet/minecraft/server/dedicated/DedicatedServer;"
		)
	)
	private static void osl$entrypoints$parseRunArgs(String[] args, CallbackInfo ci) {
		ServerLaunchEvents.PARSE_RUN_ARGS.invoker().accept(args);
	}
}
