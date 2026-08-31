package net.ornithemc.osl.biomes.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.biomes.api.biome.BiomeExtension;
import net.ornithemc.osl.biomes.impl.BiomeRegistryImpl;
import net.ornithemc.osl.biomes.impl.BiomesMixinPlugin;
import net.ornithemc.osl.biomes.impl.VanillaBiomes;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArray;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeExtension {

	@Shadow @Final @Mutable
	private static Biome[] BY_ID;

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

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$biomes$registerArrayMappers(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.BIOME, NamespacedIdentifiers.from("biome/by_id"), ArrayMapper.of(() -> BY_ID, a -> BY_ID = a));
	}

	@ModifyVariable(
		method = "<init>",
		argsOnly = true,
		ordinal = 0,
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Object;<init>()V",
			shift = Shift.AFTER
		)
	)
	private int osl$biomes$handleAutoAssignId(int id) {
		if (id == AUTO_ASSIGN_ID) {
			// the Biome[] array must contain all biomes so this should
			// give us a valid ID for the Biome registry to use.
			id = DynamicArray.length(BY_ID);

			// keep 0-255 free for all Vanilla biomes
			if (BiomesMixinPlugin.BIOME_IDS_BEYOND_255_SUPPORTED && id <= VanillaBiomes.MAX_ID) {
				id = VanillaBiomes.MAX_ID + 1;
			}
		}

		if (!BiomesMixinPlugin.BIOME_IDS_BEYOND_255_SUPPORTED && id > VanillaBiomes.MAX_ID) {
			throw new IllegalStateException("Biome IDs above 255 are not supported at this time!"
					+ " Biome IDs should be limited to the range 0-255"
					+ " unless the save format has been modified to support IDs beyond 255.");
		}

		return id;
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/biome/Biome;BY_ID:[Lnet/minecraft/world/biome/Biome;",
			opcode = Opcodes.GETSTATIC,
			args = "array=set"
		)
	)
	private void osl$biomes$growArrays(int id, CallbackInfo ci) {
		int capacity = id + 1;

		BY_ID = DynamicArray.grow(BY_ID, capacity);
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
			id = DynamicArray.length(BY_ID);
		}

		return op.call(original, id);
	}
}
