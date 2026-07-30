package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.Block;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.ornithemc.osl.items.impl.mixin.common.BlockItemAccess;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin_b1_5_12w32a {

	@ModifyExpressionValue(
		method = "renderGuiItem",
		at = @At(
			value = "CONSTANT",
			args = "intValue=256"
		)
	)
	private int osl$items$fixBlockIdCheck(int maxBlockId, @Local(ordinal = 0) int item) {
		return Item.BY_ID[item] instanceof BlockItem ? item + 1 : 0;
	}

	@Definition(
		id = "BY_ID",
		field = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;"
	)
	@Expression("BY_ID[?]")
	@WrapOperation(
		method = "renderGuiItem",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION"
		)
	)
	private Block osl$items$fixBlockCheck(Block[] BY_ID, int id, Operation<Block> op, @Local(ordinal = 0) int item) {
		return Item.BY_ID[item] instanceof BlockItem ? op.call(BY_ID, ((BlockItemAccess) Item.BY_ID[item]).accessBlock()) : null;
	}
}
