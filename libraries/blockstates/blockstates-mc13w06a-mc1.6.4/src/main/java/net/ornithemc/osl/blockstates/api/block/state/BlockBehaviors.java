package net.ornithemc.osl.blockstates.api.block.state;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.world.World;

import net.ornithemc.osl.core.api.util.math.BlockPos;
import net.ornithemc.osl.core.api.util.math.Direction;

public interface BlockBehaviors {

	boolean doEvent(World world, BlockPos pos, int type, int data);

	void neighborChanged(World world, BlockPos pos, Block neighborBlock);

	void onAdded(World world, BlockPos pos);

	void onRemoved(World world, BlockPos pos);

	void tick(World world, BlockPos pos, Random random);

	void onEntityCollision(World world, BlockPos pos, Entity entity);

	void dropItems(World world, BlockPos pos, int fortuneLevel);

	void dropItems(World world, BlockPos pos, float luck, int fortuneLevel);

	boolean use(World world, BlockPos pos, PlayerEntity player, Direction face, float faceX, float faceY, float faceZ);

	void startMining(World world, BlockPos pos, PlayerEntity player);

}
