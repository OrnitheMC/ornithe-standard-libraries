package net.ornithemc.osl.biomes.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.util.Id2ObjectBiMap;
import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.biomes.api.biome.BiomeExtension;
import net.ornithemc.osl.biomes.impl.BiomeIdRegistry;
import net.ornithemc.osl.biomes.impl.access.BiomeAccess;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeExtension, BiomeAccess {

	@Shadow @Final
	private String parent;

	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "NEW",
			target = "net/minecraft/util/Id2ObjectBiMap"
		)
	)
	private static Id2ObjectBiMap<Biome> osl$biomes$replaceIdMap() {
		// this allows us to register the muated biomes mapper in
		// the API entrypoint without triggering a Biome class load
		return BiomeIdRegistry.MUTATED_BIOMES;
	}

	@Override
	public NamespacedIdentifier osl$biomes$getParentIdentifier() {
		return this.parent == null ? null : NamespacedIdentifiers.parse(this.parent);
	}
}
