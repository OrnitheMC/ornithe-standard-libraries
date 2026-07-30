package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

import net.ornithemc.osl.items.api.ItemRegistry;

@Mixin(Minecraft.class)
public class MinecraftMixinMid {

	@ModifyVariable(
		method = "doPick",
		index = 2,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;getBlock(III)I",
			shift = Shift.BY,
			by = 2 // after local store
		)
	)
	private int osl$items$fixBlockItemId(int id, @Share("osl$items$blockId") LocalIntRef blockId) {
		blockId.set(id);

		Block block = Block.BY_ID[id];
		if (block == null) {
			return 0;
		}

		Item item = ItemRegistry.getItem(block);
		if (item == null) {
			return 0;
		}

		return item.id;
	}

	@Definition(
		id = "BY_ID",
		field = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;"
	)
	@Expression("BY_ID[?]")
	@WrapOperation(
		method = "doPick",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION"
		)
	)
	private Block osl$items$fixBlockCheck1(Block[] BY_ID, int id, Operation<Block> op, @Share("osl$items$blockId") LocalIntRef blockId) {
		return op.call(BY_ID, blockId.get());
	}
}
