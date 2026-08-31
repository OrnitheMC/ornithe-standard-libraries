package net.ornithemc.osl.biomes.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

import net.ornithemc.osl.biomes.impl.BiomeRegistryImpl;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.Id2ObjectBiMapMapper;

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
	private static void osl$biomes$registerBiomesAndMappers(CallbackInfo ci) {
		BiomeRegistryImpl.registerBiomes();

		SyncedRegistries.registerMapper(RegistryKeys.BIOME, NamespacedIdentifiers.from("mutated_biome"), Id2ObjectBiMapMapper.of(Biome.MUTATED_BIOMES));
	}
}
