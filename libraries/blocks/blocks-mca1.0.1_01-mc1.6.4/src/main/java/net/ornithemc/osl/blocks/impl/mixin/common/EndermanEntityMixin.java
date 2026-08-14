package net.ornithemc.osl.blocks.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.entity.living.mob.monster.EndermanEntity;

import net.ornithemc.osl.registries.api.registry.sync.DynamicArrays;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {

	@Shadow
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
		int capacity = Block.BY_ID.length;

		HOLDABLE_BLOCKS = DynamicArrays.grow(HOLDABLE_BLOCKS, capacity);
	}
}
