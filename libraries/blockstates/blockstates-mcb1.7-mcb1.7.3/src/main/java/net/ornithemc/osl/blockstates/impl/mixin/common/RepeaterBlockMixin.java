package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.Block;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.material.Material;

@Mixin(RepeaterBlock.class)
public class RepeaterBlockMixin extends Block {

	private RepeaterBlockMixin(int id, Material material) {
		super(id, material);
	}

	@Override
	public boolean is(Block block) {
		return block == Block.REPEATER || block == Block.POWERED_REPEATER;
	}

}
