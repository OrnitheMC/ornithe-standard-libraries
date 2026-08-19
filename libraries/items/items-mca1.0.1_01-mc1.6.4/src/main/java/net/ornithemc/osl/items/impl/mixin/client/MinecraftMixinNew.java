package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import net.ornithemc.osl.items.impl.access.BlockItemAccess;

@Mixin(Minecraft.class)
public class MinecraftMixinNew {

	@WrapOperation(
		method = "doPick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/Block;getPickItem(Lnet/minecraft/world/World;III)I"
		)
	)
	private int osl$items$captureBlock(Block block, World world, int x, int y, int z, Operation<Integer> op, @Share("osl$items$block") LocalRef<Block> capture) {
		capture.set(block);
		return op.call(block, world, x, y, z);
	}

	@Definition(
		id = "BY_ID",
		field = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;"
	)
	@Expression("BY_ID[?]")
	@WrapOperation(
		method = "doPick",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION",
			ordinal = 1
		)
	)
	private Block osl$items$fixBlockCheck1(Block[] BY_ID, int id, Operation<Block> op, @Local(index = 2) int item, @Share("osl$items$block") LocalRef<Block> block) {
		if (Item.BY_ID[item] instanceof BlockItem) {
			id = ((BlockItemAccess) Item.BY_ID[item]).osl$items$getBlock();
		} else {
			id = block.get().id;
		}

		return op.call(BY_ID, id);
	}
}
