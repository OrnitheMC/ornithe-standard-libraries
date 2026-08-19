package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.Block;

@Mixin(Block.class)
public interface BlockAccess {

	@Invoker("m_79262324")
	int osl$items$getSprite(int face);

}
