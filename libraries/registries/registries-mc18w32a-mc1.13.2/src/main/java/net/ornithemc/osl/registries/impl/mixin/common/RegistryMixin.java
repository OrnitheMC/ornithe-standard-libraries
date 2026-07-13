package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.registry.Registry;

import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;

@Mixin(Registry.class)
public interface RegistryMixin {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	static void osl$registries$initRegistries(CallbackInfo ci) {
		RegistriesImpl.init();
		SyncedRegistriesImpl.init();
	}
}
