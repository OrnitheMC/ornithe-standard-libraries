package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.LeavesBlock;

import net.ornithemc.osl.items.impl.item.ItemUtil;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

	@WrapOperation(
		method = "getSilkTouchDrop",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/LeavesBlock;id:I"
		)
	)
	private int osl$items$fixBlockItem(LeavesBlock block, Operation<Integer> op) {
		return ItemUtil.itemId(block);
	}
}
