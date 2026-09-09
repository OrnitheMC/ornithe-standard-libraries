package net.ornithemc.osl.blockstates.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.Block;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportCategory;
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
	private Profiler profiler;

	@Shadow
	private WorldChunk getChunk(int x, int z) { return null; }
	@Shadow
	private void checkLight(int x, int y, int z) { }
	@Shadow
	private void notifyBlockChanged(int x, int y, int z) { }
	@Shadow
	private void onBlockChanged(int x, int y, int z, int block) { }

	@Definition(
		id = "block",
		local = @Local(
			type = Block.class
		)
	)
	@Expression("block != null")
	@WrapOperation(
		method = "containsNonAir",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION"
		)
	)
	private boolean osl$blockstates$fixNotAirCheck(Object block, Object _null, Operation<Boolean> op) {
		return op.call(block, _null) && !((Block) block).isAir();
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

		WorldChunk chunk = null;

		try {
			chunk = this.getChunk(x, z);
			return chunk.getBlockState(x, y, z);
		} catch (Throwable t) {
			CrashReport report = CrashReport.of(t, "Exception getting block state in world");
			CrashReportCategory category = report.addCategory("Requested block coordinates");
			category.add("Found chunk", chunk == null);
			category.add("Location", CrashReportCategory.formatPosition(x, y, z));

			throw new CrashException(report);
		}
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

		Block replacedBlock = replaced.getBlock();

		if (!state.is(replacedBlock)) {
			this.profiler.push("checkLight");
			this.checkLight(x, y, z);
			this.profiler.pop();
		}

		if ((this.isMultiplayer || chunk.full) && (!state.is(replacedBlock) || state.updateClients())) {
			this.notifyBlockChanged(x, y, z);
		}

		if (!this.isMultiplayer && notify) {
			this.onBlockChanged(x, y, z, replacedBlock.id);
		}

		return true;
	}
}
