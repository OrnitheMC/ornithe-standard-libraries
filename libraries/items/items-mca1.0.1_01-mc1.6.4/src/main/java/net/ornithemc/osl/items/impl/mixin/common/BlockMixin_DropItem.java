package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.Block;

import net.ornithemc.osl.items.impl.item.ItemUtil;

@Mixin(Block.class)
public class BlockMixin_DropItem {

	@WrapOperation(
		method = "getDropItem",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/Block;id:I"
		)
	)
	private int osl$items$fixBlockItem(Block block, Operation<Integer> op) {
		return ItemUtil.itemId(block);
	}
}
