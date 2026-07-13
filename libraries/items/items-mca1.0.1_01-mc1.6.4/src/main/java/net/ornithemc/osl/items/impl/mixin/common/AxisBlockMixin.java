package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.AxisBlock;

import net.ornithemc.osl.items.impl.item.ItemUtil;

@Mixin(AxisBlock.class)
public class AxisBlockMixin {

	@WrapOperation(
		method = "getSilkTouchDrop",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/AxisBlock;id:I"
		)
	)
	private int osl$items$fixBlockItem(AxisBlock block, Operation<Integer> op) {
		return ItemUtil.itemId(block);
	}
}
