package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.Bootstrap;

import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;

@Mixin(Bootstrap.class)
public class BootstrapMixin {

	@Shadow
	private static boolean initialized;

	@WrapMethod(method = "init()V")
	private static void osl$registries$initAndLockRegistries(Operation<Void> op) {
		boolean wasInitialized = initialized;

		// we could skip the call if initialized is already true
		// but let's not mess with the bootstrap too much, eh
		op.call();

		if (!wasInitialized) {
			RegistriesImpl.init();
			SyncedRegistriesImpl.init();
		}
	}
}
