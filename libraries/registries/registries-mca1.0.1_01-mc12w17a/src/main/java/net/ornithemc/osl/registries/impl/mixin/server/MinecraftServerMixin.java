package net.ornithemc.osl.registries.impl.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.registries.impl.Bootstrap;

@Mixin(
	value = MinecraftServer.class,
	priority = 1001 // make sure entrypoints mixin is applied first
)
public class MinecraftServerMixin {

	@Inject(
		method = "main",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$registries$bootstrap(CallbackInfo ci) {
		Bootstrap.init();
	}
}
