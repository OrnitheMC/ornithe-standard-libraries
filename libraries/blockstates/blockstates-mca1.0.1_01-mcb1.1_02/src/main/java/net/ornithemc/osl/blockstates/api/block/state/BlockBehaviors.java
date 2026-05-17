package net.ornithemc.osl.blockstates.api.block.state;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.world.World;

import net.ornithemc.osl.core.api.util.math.BlockPos;

public interface BlockBehaviors {

	void neighborChanged(World world, BlockPos pos, Block neighborBlock);

	void onAdded(World world, BlockPos pos);

	void onRemoved(World world, BlockPos pos);

	void tick(World world, BlockPos pos, Random random);

	void onEntityCollision(World world, BlockPos pos, Entity entity);

	void dropItems(World world, BlockPos pos);

	void dropItems(World world, BlockPos pos, float luck);

	boolean use(World world, BlockPos pos, PlayerEntity player);

	void startMining(World world, BlockPos pos, PlayerEntity player);

}
