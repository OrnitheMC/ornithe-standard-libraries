package net.ornithemc.osl.blocks.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.AirBlock;

import net.ornithemc.osl.blocks.api.block.BlockExtension;

@Mixin(AirBlock.class)
public class AirBlockMixin implements BlockExtension {

	@Override
	public boolean isAir() {
		return true;
	}
}
