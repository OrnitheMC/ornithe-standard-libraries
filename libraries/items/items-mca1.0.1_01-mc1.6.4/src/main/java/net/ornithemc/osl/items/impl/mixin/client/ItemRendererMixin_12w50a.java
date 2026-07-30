package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.Block;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import net.ornithemc.osl.items.impl.mixin.common.BlockItemAccess;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin_12w50a {

	@Definition(
		id = "BY_ID",
		field = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;"
	)
	@Expression("BY_ID[?]")
	@WrapOperation(
		method = "m_29207578",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION"
		)
	)
	private Block osl$items$fixBlockCheck(Block[] BY_ID, int id, Operation<Block> op, @Local ItemStack item) {
		return item.getItem() instanceof BlockItem ? op.call(BY_ID, ((BlockItemAccess) item.getItem()).accessBlock()) : null;
	}
}
