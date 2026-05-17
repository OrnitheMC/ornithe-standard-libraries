package net.ornithemc.osl.blockstates.api.block.state;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public interface BlockProperties {

	boolean isAir();

	boolean ticksRandomly();

	Box getShape(WorldView world, BlockPos pos);

	boolean canWalkThrough(WorldView world, BlockPos pos);

	boolean canBeReplaced(WorldView world, BlockPos pos);

	boolean canBePlaced(World world, BlockPos pos);

}
