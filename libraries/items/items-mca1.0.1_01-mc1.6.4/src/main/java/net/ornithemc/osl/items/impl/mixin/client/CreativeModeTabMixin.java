package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.Block;

import net.ornithemc.osl.items.impl.item.ItemUtil;

@Mixin(targets = {
	"net/minecraft/item/CreativeModeTab$98745988", // transportation
	"net/minecraft/item/CreativeModeTab$27543685"  // building blocks
})
public class CreativeModeTabMixin {

	@WrapOperation(
		method = "getIconItem",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/Block;id:I"
		)
	)
	private int osl$items$fixIconItem(Block block, Operation<Integer> op) {
		return ItemUtil.itemId(block);
	}
}
