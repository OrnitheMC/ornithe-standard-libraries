package net.ornithemc.osl.registries.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.integrated.IntegratedServer;

import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

	@Inject(
		method = "shutdown",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$registries$unmapRegistries(CallbackInfo ci) {
		SyncedRegistriesImpl.undoMappings();
	}
}
