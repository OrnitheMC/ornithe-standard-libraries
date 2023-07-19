package net.ornithemc.osl.entrypoints.impl.mixin.server;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerLaunchEvents;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject(
		method = "main",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$entrypoints$init(String[] args, CallbackInfo ci) {
		EntrypointUtils.invoke(
			ServerModInitializer.ENTRYPOINT_KEY,
			ServerModInitializer.class,
			ServerModInitializer::initServer
		);
		EntrypointUtils.invoke(
			ModInitializer.ENTRYPOINT_KEY,
			ModInitializer.class,
			ModInitializer::init
		);

		ServerLaunchEvents.PARSE_RUN_ARGS.invoker().accept(Arrays.copyOf(args, args.length));
	}
}
