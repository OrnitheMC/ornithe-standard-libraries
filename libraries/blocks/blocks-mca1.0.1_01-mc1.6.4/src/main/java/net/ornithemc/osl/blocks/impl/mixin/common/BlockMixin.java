package net.ornithemc.osl.blocks.impl.mixin.common;

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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import net.ornithemc.osl.blocks.api.block.BlockExtension;
import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;
import net.ornithemc.osl.blocks.impl.VanillaBlocks;
import net.ornithemc.osl.blocks.impl.block.BlockPostInit;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.BooleanArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArrays;
import net.ornithemc.osl.registries.api.registry.sync.IntArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.ObjectArrayMapper;

@Mixin(Block.class)
public abstract class BlockMixin implements BlockExtension {

	@Shadow @Final @Mutable
	private static Block[] BY_ID;
	@Shadow @Final @Mutable
	private static boolean[] IS_SOLID_RENDER;
	@Shadow @Final @Mutable
	private static int[] OPACITIES;
	@Shadow @Final @Mutable
	private static boolean[] IS_TRANSLUCENT;
	@Shadow @Final @Mutable
	private static int[] LIGHT;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$blocks$unlockBlockRegistry(CallbackInfo ci) {
		BlockRegistryImpl.unlock();
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			// inject before the for-loop for checking USES_NEIGHBOR_LIGHT
			value = "JUMP",
			opcode = Opcodes.IF_ICMPGE,
			shift = Shift.BY,
			// it's extreme but this ensures that the injector is only invoked once
			by = -5
		)
	)
	private static void osl$blocks$registerBlocks(CallbackInfo ci) {
		// in Beta 1.2 and above Item class init will happen before this point
		// but that should not matter as there is a separate event for block
		// item registration
		BlockRegistryImpl.registerBlocks();
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$blocks$registerArrayMappers(CallbackInfo ci) {
		BlockRegistryImpl.registerUnknownBlocks();

		SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("block/by_id"), ObjectArrayMapper.of(BY_ID));
		if (MinecraftVersion.resolve().compareTo("b1.5_02") <= 0) {
			SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("block/is_solid"), BooleanArrayMapper.of(IS_SOLID_RENDER));
		} else {
			SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("block/is_solid_render"), BooleanArrayMapper.of(IS_SOLID_RENDER));
		}
		SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("block/opacity"), IntArrayMapper.of(OPACITIES));
		SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("block/is_translucent"), BooleanArrayMapper.of(IS_TRANSLUCENT));
		SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("block/light"), IntArrayMapper.of(LIGHT));

		for (Block block : BY_ID) {
			if (block instanceof BlockPostInit) {
				((BlockPostInit) block).osl$blocks$postInit();
			}
		}
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
	private int osl$blocks$handleAutoAssignId(int id) {
		if (id == AUTO_ASSIGN_ID) {
			// the Block[] array must contain all blocks so this should
			// give us a valid ID for the Block registry to use.
			id = DynamicArrays.length(BY_ID);

			// keep 0-255 free for all Vanilla blocks
			if (id <= VanillaBlocks.MAX_ID) {
				id = VanillaBlocks.MAX_ID + 1;
			}
		}

		return id;
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

		BY_ID = DynamicArrays.grow(BY_ID, capacity);
		IS_SOLID_RENDER = DynamicArrays.grow(IS_SOLID_RENDER, capacity);
		OPACITIES = DynamicArrays.grow(OPACITIES, capacity);
		IS_TRANSLUCENT = DynamicArrays.grow(IS_TRANSLUCENT, capacity);
		LIGHT = DynamicArrays.grow(LIGHT, capacity);
	}

	@Override
	public String toString() {
		return "Block{" + BlockRegistryImpl.getIdentifier((Block) (Object) this) + "}";
	}
}
