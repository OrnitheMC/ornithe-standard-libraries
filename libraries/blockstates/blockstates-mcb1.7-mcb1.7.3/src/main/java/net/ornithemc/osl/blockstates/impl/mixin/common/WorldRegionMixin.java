package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.WorldRegion;
import net.minecraft.world.chunk.WorldChunk;

import net.ornithemc.osl.blockstates.api.block.Blocks;
import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.world.WorldViewExtension;
import net.ornithemc.osl.core.api.util.math.BlockPos;

@Mixin(WorldRegion.class)
public class WorldRegionMixin implements WorldViewExtension {

	@Shadow
	private int chunkX;
	@Shadow
	private int chunkZ;
	@Shadow
	private WorldChunk[][] chunks;

	@Override
	public BlockState getBlockState(BlockPos pos) {
		return this.getBlockState(pos.x(), pos.y(), pos.z());
	}

	@Override
	public BlockState getBlockState(int x, int y, int z) {
		if (WorldViewExtension.isInsideWorldHeight(y)) {
			int indexX = (x >> 4) - this.chunkX;
			int indexZ = (z >> 4) - this.chunkZ;

			if (indexX >= 0 && indexX < this.chunks.length && indexZ >= 0 && indexZ < this.chunks[indexX].length) {
				WorldChunk chunk = this.chunks[indexX][indexZ];

				if (chunk != null) {
					return chunk.getBlockState(x, y, z);
				}
			}
		}

		return Blocks.AIR.defaultState();
	}
}
