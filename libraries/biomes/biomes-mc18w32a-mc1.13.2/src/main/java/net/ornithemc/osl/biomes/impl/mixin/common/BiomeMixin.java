package net.ornithemc.osl.biomes.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.biomes.api.biome.BiomeExtension;
import net.ornithemc.osl.biomes.impl.BiomeRegistryImpl;
import net.ornithemc.osl.biomes.impl.access.BiomeAccess;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeExtension, BiomeAccess {

	@Shadow @Final
	private String parent;

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
	private static void osl$biomes$registerBiomes(CallbackInfo ci) {
		BiomeRegistryImpl.registerBiomes();
	}

	@Override
	public NamespacedIdentifier osl$biomes$getParentIdentifier() {
		return this.parent == null ? null : NamespacedIdentifiers.parse(this.parent);
	}
}
