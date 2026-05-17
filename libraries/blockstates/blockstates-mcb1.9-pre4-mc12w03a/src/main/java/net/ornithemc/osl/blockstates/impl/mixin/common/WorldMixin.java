package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.block.Block;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import net.ornithemc.osl.blockstates.api.block.Blocks;
import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.world.WorldExtension;
import net.ornithemc.osl.core.api.util.math.BlockPos;

@Mixin(World.class)
public class WorldMixin implements WorldExtension {

	@Shadow
	private boolean isMultiplayer;

	@Shadow
	private WorldChunk getChunk(int x, int z) { return null; }
	@Shadow
	private void checkLight(int x, int y, int z) { }
	@Shadow
	private void onBlockChanged(int x, int y, int z, int block) { }
	@Shadow
	private void updateNeighbors(int x, int y, int z, int block) { }

	@ModifyVariable(
		method = "canPlace",
		ordinal = 0,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/Block;getCollisionShape(Lnet/minecraft/world/World;III)Lnet/minecraft/util/math/Box;"
		)
	)
	private Block osl$blockstates$makeAirReplaceable(Block block) {
		return block == Blocks.AIR ? null : block;
	}

	@Override
	public WorldChunk getChunk(BlockPos pos) {
		return this.getChunk(pos.x(), pos.z());
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		return this.getBlockState(pos.x(), pos.y(), pos.z());
	}

	@Override
	public BlockState getBlockState(int x, int y, int z) {
		if (!WorldExtension.isInsideWorld(x, y, z)) {
			return Blocks.AIR.defaultState();
		}

		WorldChunk chunk = this.getChunk(x, z);
		return chunk.getBlockState(x, y, z);
	}

	@Override
	public boolean setBlockState(BlockPos pos, BlockState state, boolean notify) {
		return this.setBlockState(pos.x(), pos.y(), pos.z(), state, notify);
	}

	@Override
	public boolean setBlockState(int x, int y, int z, BlockState state, boolean notify) {
		if (!WorldExtension.isInsideWorld(x, y, z)) {
			return false;
		}

		WorldChunk chunk = this.getChunk(x, z);
		BlockState replaced = chunk.setBlockState(x, y, z, state);

		if (replaced == null) {
			return false;
		}

		Block block = state.getBlock();
		Block replacedBlock = replaced.getBlock();

		if (!state.is(replacedBlock)) {
			Profiler.push("checkLight");
			this.checkLight(x, y, z);
			Profiler.pop();
		}

		if (!this.isMultiplayer && notify) {
			if (!state.is(replacedBlock) || state.updateClients()) {
				this.onBlockChanged(x, y, z, block.id);
			} else {
				this.updateNeighbors(x, y, z, block.id);
			}
		}

		return true;
	}
}
