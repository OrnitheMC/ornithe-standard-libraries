package net.ornithemc.osl.biomes.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.biomes.api.biome.BiomeExtension;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

@Mixin(Biome.Builder.class)
public class BiomeBuilderMixin implements BiomeExtension.BuilderExtension {

	@Shadow
	public String parent;

	@Override
	public Biome.Builder parent(NamespacedIdentifier identifier) {
		this.parent = identifier.toString();
		return (Biome.Builder) (Object) this;
	}
}
