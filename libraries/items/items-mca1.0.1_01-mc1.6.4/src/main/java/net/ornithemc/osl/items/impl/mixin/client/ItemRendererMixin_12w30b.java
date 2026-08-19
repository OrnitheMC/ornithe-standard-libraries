package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin_12w30b {

	@ModifyExpressionValue(
		method = "render",
		at = @At(
			value = "CONSTANT",
			args = "intValue=256"
		)
	)
	private int osl$items$fixBlockIdCheck(int maxBlockId, @Local ItemStack item) {
		return item.getItem() instanceof BlockItem ? item.id + 1 : 0;
	}
}
