package net.ornithemc.osl.biomes.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.biomes.api.biome.BiomeExtension;
import net.ornithemc.osl.biomes.impl.access.BiomeAccess;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeExtension, BiomeAccess {

	@Shadow @Final
	private String parent;

	@Override
	public NamespacedIdentifier osl$biomes$getParentIdentifier() {
		return this.parent == null ? null : NamespacedIdentifiers.parse(this.parent);
	}
}
