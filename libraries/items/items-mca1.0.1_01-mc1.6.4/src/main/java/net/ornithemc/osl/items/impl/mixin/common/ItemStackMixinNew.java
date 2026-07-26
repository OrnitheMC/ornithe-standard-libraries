package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.ornithemc.osl.items.api.ItemRegistry;

@Mixin(ItemStack.class)
public class ItemStackMixinNew {

	@Shadow
	private int id;

	@Inject(
		method = "<init>(Lnet/minecraft/block/Block;II)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$items$fixBlockItemId(Block block, int size, int metadata, CallbackInfo ci) {
		Item item = ItemRegistry.getItem(block);
		this.id = (item == null) ? 0 : ItemRegistry.getId(item);
	}
}
