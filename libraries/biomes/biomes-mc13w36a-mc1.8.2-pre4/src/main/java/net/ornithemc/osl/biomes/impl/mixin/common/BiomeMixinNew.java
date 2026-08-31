package net.ornithemc.osl.biomes.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.biomes.impl.BiomeRegistryImpl;
import net.ornithemc.osl.biomes.impl.BiomesMixinPlugin;
import net.ornithemc.osl.biomes.impl.VanillaBiomes;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArray;

@Mixin(Biome.class)
public class BiomeMixinNew {

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
			value = "FIELD",
			target = "Lnet/minecraft/world/biome/Biome;BY_ID:[Lnet/minecraft/world/biome/Biome;",
			opcode = Opcodes.GETSTATIC,
			ordinal = 0
		)
	)
	private static void osl$biomes$registerBiomes(CallbackInfo ci) {
		BiomeRegistryImpl.registerBiomes();
		BiomeRegistryImpl.registerUnknownBiomes();
	}

	@WrapOperation(
		method = "mutate()Lnet/minecraft/world/biome/Biome;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/biome/Biome;mutate(I)Lnet/minecraft/world/biome/Biome;"
		)
	)
	private static Biome osl$biomes$handleAutoAssignIdForMutatedBiomes(Biome original, int id, Operation<Biome> op) {
		if (BiomesMixinPlugin.BIOME_IDS_BEYOND_255_SUPPORTED && original.id > VanillaBiomes.MAX_ID) {
			// the Biome[] array must contain all biomes so this should
			// give us a valid ID for the Biome registry to use.
			id = DynamicArray.length(Biome.BY_ID);
		}

		return op.call(original, id);
	}
}
