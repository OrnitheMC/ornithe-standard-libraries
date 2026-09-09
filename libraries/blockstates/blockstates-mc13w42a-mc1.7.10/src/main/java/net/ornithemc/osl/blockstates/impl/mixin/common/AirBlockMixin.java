package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.state.StateDefinition;

@Mixin(AirBlock.class)
public class AirBlockMixin extends Block {

	private AirBlockMixin() {
		super(null);
	}

	@Override
	public void buildStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
	}

	@Override
	public BlockState getStateFromMetadata(int metadata) {
		return this.defaultState();
	}

	@Override
	public int getMetadataFromState(BlockState state) {
		return 0;
	}

	@Override
	public boolean isAir() {
		return true;
	}
}
