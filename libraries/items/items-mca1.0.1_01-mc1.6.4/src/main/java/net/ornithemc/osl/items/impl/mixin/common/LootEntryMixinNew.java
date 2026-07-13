package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.LootEntry;

import net.ornithemc.osl.items.impl.LootEntryAccess;

@Mixin(LootEntry.class)
public abstract class LootEntryMixinNew implements LootEntryAccess {

	@Shadow
	private ItemStack item;

	@Override
	public ItemStack osl$items$getItemStack() {
		return this.item;
	}
}
