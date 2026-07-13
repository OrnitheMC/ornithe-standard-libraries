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

@Mixin(ItemRenderer.class)
public class ItemRendererMixin_1_6 {

	@Definition(
		id = "BY_ID",
		field = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;"
	)
	@Expression("BY_ID.length")
	@WrapOperation(
		method = "render",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION"
		)
	)
	private int osl$items$fixBlockIdCheck(Block[] BY_ID, Operation<Integer> op, @Local ItemStack item) {
		return item.getItem() instanceof BlockItem ? op.call((Object) BY_ID) : 0;
	}
}
