package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.ornithemc.osl.items.impl.mixin.common.BlockItemAccess;

@Mixin(Minecraft.class)
public class MinecraftMixinNew {

	@ModifyVariable(
		method = "doPick",
		index = 2,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/Item;hasCustomData()Z",
			shift = Shift.AFTER
		)
	)
	private int osl$items$fixBlockId(int item) {
		return Item.BY_ID[item] instanceof BlockItem ? ((BlockItemAccess) Item.BY_ID[item]).accessBlock() : 0;
	}
}
