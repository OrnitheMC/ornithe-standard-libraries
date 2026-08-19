package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.ornithemc.osl.items.impl.access.BlockItemAccess;

@Mixin(Minecraft.class)
public class MinecraftMixinNewNew {

	@Inject(
		method = "doPick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/Item;hasCustomData()Z"
		)
	)
	private void osl$items$captureBlock(CallbackInfo ci, @Local Block block, @Share("osl$items$block") LocalRef<Block> capture) {
		capture.set(block);
	}

	@ModifyVariable(
		method = "doPick",
		index = 9,
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;",
			ordinal = 2
		)
	)
	private int osl$items$fixBlockId(int blockId, @Share("osl$items$block") LocalRef<Block> block, @Local(index = 2) int item) {
		if (Item.BY_ID[item] instanceof BlockItem && !block.get().hasPickItemMetadata()) {
			blockId = ((BlockItemAccess) Item.BY_ID[item]).osl$items$getBlock();
		}

		return blockId;
	}
}
