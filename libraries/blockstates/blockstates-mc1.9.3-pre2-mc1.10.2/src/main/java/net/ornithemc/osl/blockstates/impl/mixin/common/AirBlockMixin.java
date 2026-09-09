package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;

@Mixin(AirBlock.class)
public class AirBlockMixin extends Block {

	private AirBlockMixin() {
		super(null);
	}

	@Override
	public boolean isAir() {
		return true;
	}
}
