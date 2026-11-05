package net.ornithemc.osl.registries.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.registry.Registry;

import net.ornithemc.osl.registries.impl.RegistryEntrypoints;

@Mixin(Registry.class)
public class RegistryMixin {

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/util/registry/Registry;f_81997657:Ljava/util/Map;",
			opcode = Opcodes.GETSTATIC
		)
	)
	private static void osl$registries$initRegistries(CallbackInfo ci) {
		RegistryEntrypoints.invoke();
	}
}
