package net.ornithemc.osl.blockstates.impl.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.state.StateDefinition;

public class AirBlock extends Block {

	public AirBlock(int id) {
		super(id, Material.AIR);
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

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public Box getCollisionShape(World world, int x, int y, int z) {
		return null;
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean canRayTrace(int metadata, boolean allowLiquids) {
		return false;
	}
}
