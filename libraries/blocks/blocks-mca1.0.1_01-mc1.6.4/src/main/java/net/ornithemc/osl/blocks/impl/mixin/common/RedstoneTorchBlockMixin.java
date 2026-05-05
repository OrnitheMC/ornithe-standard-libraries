package net.ornithemc.osl.blocks.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.Block;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.material.Material;

@Mixin(RedstoneTorchBlock.class)
public class RedstoneTorchBlockMixin extends Block {

	private RedstoneTorchBlockMixin(int id, Material material) {
		super(id, material);
	}

	@Override
	public boolean is(Block block) {
		return block == Block.REDSTONE_TORCH || block == Block.UNLIT_REDSTONE_TORCH;
	}

}
