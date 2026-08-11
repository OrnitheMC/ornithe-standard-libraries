package net.ornithemc.osl.blocks.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.entity.living.mob.monster.EndermanEntity;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.BooleanArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArrays;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {

	// what the fuck, mojang?
	@Shadow @Final @Mutable
	private static boolean[] HOLDABLE_BLOCKS;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/entity/living/mob/monster/EndermanEntity;HOLDABLE_BLOCKS:[Z",
			opcode = Opcodes.PUTSTATIC,
			shift = Shift.AFTER
		)
	)
	private static void osl$blocks$growArray(CallbackInfo ci) {
		int capacity = HOLDABLE_BLOCKS.length;

		for (Block block : Block.REGISTRY) {
			int required = Block.getId(block) + 1;

			if (required > capacity) {
				capacity = required;
			}
		}

		HOLDABLE_BLOCKS = DynamicArrays.grow(HOLDABLE_BLOCKS, capacity);
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$blocks$registerArrayMapper(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("block/enderman_holdable"), BooleanArrayMapper.of(HOLDABLE_BLOCKS));
	}
}
