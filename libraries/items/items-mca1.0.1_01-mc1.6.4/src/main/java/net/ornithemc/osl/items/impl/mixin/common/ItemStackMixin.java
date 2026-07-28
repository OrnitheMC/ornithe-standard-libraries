package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import net.ornithemc.osl.items.impl.item.ItemUtil;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	@Shadow
	private int id;

	@Inject(
		method = "<init>(Lnet/minecraft/block/Block;I)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$items$fixBlockItemId(Block block, int size, CallbackInfo ci) {
		this.id = ItemUtil.itemId(block);
	}
}
