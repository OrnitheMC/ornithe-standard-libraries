package net.ornithemc.osl.items.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.Block;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

@Mixin(FurnaceBlockEntity.class)
public class FurnaceBlockEntityMixin {

	@ModifyExpressionValue(
		method = "getFuelTime",
		at = @At(
			value = "CONSTANT",
			args = "intValue=256"
		)
	)
	private int osl$items$fixBlockIdCheck(int maxBlockId, @Local ItemStack item) {
		return item.getItem() instanceof BlockItem ? item.id + 1 : 0;
	}

	@WrapOperation(
		method = "getFuelTime",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;",
			opcode = Opcodes.GETSTATIC,
			args = "array=get"
		)
	)
	private Block osl$items$fixBlockCheck(Block[] BY_ID, int id, Operation<Block> op, @Local ItemStack item) {
		return item.getItem() instanceof BlockItem ? op.call(BY_ID, ((BlockItem) item.getItem()).getBlock()) : null;
	}
}
