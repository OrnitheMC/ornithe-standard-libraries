package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.stat.ItemStat;

@Mixin(ItemStat.class)
public interface ItemStatAccess {

	@Accessor("item")
	int accessItem();

	@Mutable
	@Accessor("item")
	void setItem(int item);

}
