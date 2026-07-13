package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.gen.structure.LootEntry;

import net.ornithemc.osl.items.impl.LootEntryAccess;

@Mixin(LootEntry.class)
public abstract class LootEntryMixinOld implements LootEntryAccess {

	@Shadow
	private int f_82867366; // item

	@Override
	public int osl$items$getItem() {
		return this.f_82867366;
	}

	@Override
	public void osl$items$setItem(int item) {
		this.f_82867366 = item;
	}
}
