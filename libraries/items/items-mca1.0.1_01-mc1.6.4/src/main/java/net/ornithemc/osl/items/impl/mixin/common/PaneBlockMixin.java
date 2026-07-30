package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.PaneBlock;

import net.ornithemc.osl.items.impl.item.ItemUtil;

@Mixin(PaneBlock.class)
public class PaneBlockMixin {

	@WrapOperation(
		method = "getSilkTouchDrop",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/PaneBlock;id:I"
		)
	)
	private int osl$items$fixBlockItem(PaneBlock block, Operation<Integer> op) {
		return ItemUtil.itemId(block);
	}
}
