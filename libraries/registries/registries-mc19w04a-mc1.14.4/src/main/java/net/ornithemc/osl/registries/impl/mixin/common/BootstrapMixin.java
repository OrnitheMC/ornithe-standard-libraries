package net.ornithemc.osl.registries.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.Bootstrap;
import net.minecraft.util.registry.Registry;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;

@Mixin(Bootstrap.class)
public class BootstrapMixin {

	@Inject(
		method = "init",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/util/registry/Registry;REGISTRY:Lnet/minecraft/unmapped/C_06803903;",
			opcode = Opcodes.GETSTATIC
		)
	)
	private static void osl$registries$initRegistries(CallbackInfo ci) {
		// force registry loading (the targeted field access does the same)
		Registry.REGISTRY.getClass();

		RegistriesImpl.init();
		SyncedRegistriesImpl.init();
	}
}
