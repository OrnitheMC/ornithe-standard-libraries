package net.ornithemc.osl.entrypoints.impl.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.entrypoints.impl.launch.LaunchUtils;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject(
		method = "main",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$entrypoints$init(String[] args, CallbackInfo ci) {
		FabricLoader.getInstance().invokeEntrypoints(
			ModInitializer.ENTRYPOINT_KEY,
			ModInitializer.class,
			ModInitializer::init
		);
		FabricLoader.getInstance().invokeEntrypoints(
			ServerModInitializer.ENTRYPOINT_KEY,
			ServerModInitializer.class,
			ServerModInitializer::initServer
		);

		LaunchUtils.triggerLaunchEvents(args);
	}
}
