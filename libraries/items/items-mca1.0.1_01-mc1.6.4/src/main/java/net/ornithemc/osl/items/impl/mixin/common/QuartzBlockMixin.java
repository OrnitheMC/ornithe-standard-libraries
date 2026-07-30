package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.QuartzBlock;

import net.ornithemc.osl.items.impl.item.ItemUtil;

@Mixin(QuartzBlock.class)
public class QuartzBlockMixin {

	@WrapOperation(
		method = "getSilkTouchDrop",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/QuartzBlock;id:I"
		)
	)
	private int osl$items$fixBlockItem(QuartzBlock block, Operation<Integer> op) {
		return ItemUtil.itemId(block);
	}
}
