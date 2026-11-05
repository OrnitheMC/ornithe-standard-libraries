package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.Bootstrap;

import net.ornithemc.osl.registries.impl.RegistryEntrypoints;

@Mixin(Bootstrap.class)
public class BootstrapMixin {

	@Shadow
	private static boolean initialized;

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$registries$initRegistries(CallbackInfo ci) {
		if (!initialized) {
			RegistryEntrypoints.invoke();
		}
	}
}
