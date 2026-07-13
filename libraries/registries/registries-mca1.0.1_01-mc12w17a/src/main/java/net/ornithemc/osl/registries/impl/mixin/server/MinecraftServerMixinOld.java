package net.ornithemc.osl.registries.impl.mixin.server;

import java.io.File;
import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import net.ornithemc.osl.registries.impl.mixin.client.WorldAccessOld;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingStorage;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixinOld {

	@Shadow
	private ServerWorld f_58488472;

	@Inject(
		method = "saveWorlds",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$registries$saveRegistryMappings(CallbackInfo ci) {
		File dir = ((WorldAccessOld) this.f_58488472).accessSaveDirectory();

		try {
			RegistryMappingStorage.saveRegistryMappings(dir);
		} catch (IOException e) {
			RegistriesImpl.LOGGER.error("Exception saving level", e);
		}
	}
}
