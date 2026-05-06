package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.item.BlockItem;

@Mixin(BlockItem.class)
public interface BlockItemAccess {

	@Accessor("block")
	int accessBlock();

}
