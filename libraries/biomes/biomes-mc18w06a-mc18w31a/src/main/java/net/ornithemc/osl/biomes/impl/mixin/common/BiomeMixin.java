package net.ornithemc.osl.biomes.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.Identifier;
import net.minecraft.util.Id2ObjectBiMap;
import net.minecraft.util.registry.IdRegistry;
import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.biomes.api.biome.BiomeExtension;
import net.ornithemc.osl.biomes.impl.BiomeIdRegistry;
import net.ornithemc.osl.biomes.impl.BiomeRegistryImpl;
import net.ornithemc.osl.biomes.impl.access.BiomeAccess;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.Id2ObjectBiMapMapper;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeExtension, BiomeAccess {

	@Shadow @Final
	public static Id2ObjectBiMap<Biome> MUTATED_BIOMES;

	@Shadow @Final
	private String parent;

	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "NEW",
			target = "net/minecraft/util/registry/IdRegistry"
		)
	)
	private static IdRegistry<Identifier, Biome> osl$biomes$replaceIdRegistry() {
		// this allows us to register the biome registry in
		// the API entrypoint without triggering a Biome class load
		return BiomeIdRegistry.REGISTRY;
	}

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$biomes$unlockBiomeRegistry(CallbackInfo ci) {
		BiomeRegistryImpl.unlock();
	}

	@Inject(
		method = "init",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$biomes$registerBiomesAndMappers(CallbackInfo ci) {
		BiomeRegistryImpl.registerBiomes();

		SyncedRegistries.registerMapper(RegistryKeys.BIOME, NamespacedIdentifiers.from("mutated_biome"), Id2ObjectBiMapMapper.of(MUTATED_BIOMES));
	}

	@Override
	public NamespacedIdentifier osl$biomes$getParentIdentifier() {
		return this.parent == null ? null : NamespacedIdentifiers.parse(this.parent);
	}
}
