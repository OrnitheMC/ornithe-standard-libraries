package net.ornithemc.osl.blocks.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import net.ornithemc.osl.blocks.api.block.BlockExtension;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.BooleanArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.DynamicBooleanArray;

@Mixin(Block.class)
public abstract class BlockMixin_a1_1_0_12w06a implements BlockExtension {

	@Shadow @Final @Mutable
	private static boolean[] f_72646756;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$blocks$registerArrayMappers(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("block/has_block_entity"), BooleanArrayMapper.of(() -> f_72646756, a -> f_72646756 = a));
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;",
			opcode = Opcodes.GETSTATIC,
			args = "array=set"
		)
	)
	private void osl$blocks$growArrays(int id, Material material, CallbackInfo ci) {
		int capacity = id + 1;

		f_72646756 = DynamicBooleanArray.grow(f_72646756, capacity);
	}
}
