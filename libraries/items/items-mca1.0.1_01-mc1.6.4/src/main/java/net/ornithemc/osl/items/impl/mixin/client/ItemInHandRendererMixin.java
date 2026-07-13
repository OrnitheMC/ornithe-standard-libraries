package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.Block;
import net.minecraft.client.render.ItemInHandRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import net.ornithemc.osl.items.impl.access.BlockItemAccess;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

	@Definition(
		id = "BY_ID",
		field = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;"
	)
	@Expression("BY_ID[?]")
	@WrapOperation(
		method = "render",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION"
		)
	)
	private Block osl$items$fixBlockCheck(Block[] BY_ID, int id, Operation<Block> op, @Local ItemStack item) {
		return item.getItem() instanceof BlockItem ? op.call(BY_ID, ((BlockItemAccess) item.getItem()).osl$items$getBlock()) : null;
	}
}
