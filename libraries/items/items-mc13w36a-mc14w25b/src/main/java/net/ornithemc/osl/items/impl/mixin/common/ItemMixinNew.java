package net.ornithemc.osl.items.impl.mixin.common;

import java.util.HashSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.ornithemc.osl.items.impl.ItemRegistryImpl;

@Mixin(Item.class)
public class ItemMixinNew {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$items$unlockItemRegistry(CallbackInfo ci) {
		ItemRegistryImpl.unlock();
	}

	@Inject(
		method = "init",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/registry/IdRegistry;keySet()Ljava/util/Set;"
		)
	)
	private static void osl$items$registerItems(CallbackInfo ci, @Local HashSet<Block> blocksToSkip) {
		ItemRegistryImpl.registerItems();

		// this set contains all blocks for which not to auto-generate block items
		blocksToSkip.addAll(ItemRegistryImpl.BLOCK_ITEMS.keySet());
	}
}
