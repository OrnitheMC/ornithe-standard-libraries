package net.ornithemc.osl.biomes.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.biome.Biomes;

import net.ornithemc.osl.biomes.impl.BiomeRegistryImpl;

@Mixin(Biomes.class)
public class BiomesMixin {

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$biomes$unlockBiomeRegistry(CallbackInfo ci) {
		BiomeRegistryImpl.unlock();
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$biomes$registerBiomes(CallbackInfo ci) {
		BiomeRegistryImpl.registerBiomes();
	}
}
