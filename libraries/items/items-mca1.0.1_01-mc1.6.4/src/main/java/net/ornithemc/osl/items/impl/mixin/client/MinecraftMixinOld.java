package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

import net.ornithemc.osl.items.api.ItemRegistry;

@Mixin(Minecraft.class)
public class MinecraftMixinOld {

	@ModifyVariable(
		method = "doPick",
		index = 1,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;getBlock(III)I",
			shift = Shift.BY,
			by = 2 // after local store
		)
	)
	private int osl$items$fixBlockItemId(int id) {
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
}
