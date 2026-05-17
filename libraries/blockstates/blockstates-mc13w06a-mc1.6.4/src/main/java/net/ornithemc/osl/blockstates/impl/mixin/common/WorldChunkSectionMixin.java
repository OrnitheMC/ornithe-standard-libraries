package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.WorldChunkSection;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.world.WorldChunkSectionExtension;

@Mixin(WorldChunkSection.class)
public class WorldChunkSectionMixin implements WorldChunkSectionExtension {

	@Shadow
	private int getBlock(int x, int y, int z) { return 0; }
	@Shadow
	private int getBlockMetadata(int x, int y, int z) { return 0; }
	@Shadow
	private void setBlock(int x, int y, int z, int block) { }
	@Shadow
	private void setBlockMetadata(int x, int y, int z, int metadata) { }

	@Override
	public BlockState getBlockState(int x, int y, int z) {
		Block block = Block.BY_ID[this.getBlock(x, y, z)];
		int metadata = this.getBlockMetadata(x, y, z);

		return block.getStateFromMetadata(metadata);
	}

	@Override
	public void setBlockState(int x, int y, int z, BlockState state) {
		Block block = state.getBlock();
		int metadata = block.getMetadataFromState(state);

		this.setBlock(x, y, z, block.id);
		this.setBlockMetadata(x, y, z, metadata);
	}
}
