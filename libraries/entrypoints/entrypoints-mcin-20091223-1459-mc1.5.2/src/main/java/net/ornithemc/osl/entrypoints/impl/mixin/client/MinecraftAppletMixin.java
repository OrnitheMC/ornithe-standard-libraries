package net.ornithemc.osl.entrypoints.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.MinecraftApplet;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.impl.launch.LaunchUtils;

@Mixin(MinecraftApplet.class)
public class MinecraftAppletMixin {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$entrypoints$init(CallbackInfo ci) {
		FabricLoader.getInstance().invokeEntrypoints(
			ClientModInitializer.ENTRYPOINT_KEY,
			ClientModInitializer.class,
			ClientModInitializer::initClient
		);
		FabricLoader.getInstance().invokeEntrypoints(
			ModInitializer.ENTRYPOINT_KEY,
			ModInitializer.class,
			ModInitializer::init
		);

		LaunchUtils.triggerLaunchEvents(FabricLoader.getInstance().getLaunchArguments(false));
	}
}
