package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.Bootstrap;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;

@Mixin(Bootstrap.class)
public class BootstrapMixin {

	@Inject(
		method = "init",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/stat/Stats;init()V"
		)
	)
	private static void osl$registries$initAndLockRegistries(CallbackInfo ci) {
		RegistriesImpl.init();
		SyncedRegistriesImpl.init();
	}
}
