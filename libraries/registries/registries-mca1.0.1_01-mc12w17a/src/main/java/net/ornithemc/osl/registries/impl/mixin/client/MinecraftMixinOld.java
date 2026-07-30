package net.ornithemc.osl.registries.impl.mixin.client;

import java.io.File;
import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingStorage;

@Mixin(Minecraft.class)
public class MinecraftMixinOld {

	@Shadow
	private World world;

	@Inject(
		method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;)V",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$registries$saveRegistryMappings(World world, String message, CallbackInfo ci) {
		if (this.world != null && !this.world.isMultiplayer && world == null) {
			File dir = ((WorldAccessOld) this.world).accessSaveDirectory();

			try {
				RegistryMappingStorage.saveRegistryMappings(dir);
			} catch (IOException e) {
				RegistriesImpl.LOGGER.error("Exception saving level", e);
			}
		}
	}
}
