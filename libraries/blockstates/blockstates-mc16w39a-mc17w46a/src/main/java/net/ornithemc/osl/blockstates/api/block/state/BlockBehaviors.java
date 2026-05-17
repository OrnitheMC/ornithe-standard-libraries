package net.ornithemc.osl.blockstates.api.block.state;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.World;

public interface BlockBehaviors {

	void onAdded(World world, BlockPos pos);

	void onRemoved(World world, BlockPos pos);

	void tick(World world, BlockPos pos, Random random);

	void randomTick(World world, BlockPos pos, Random random);

	void onEntityCollision(World world, BlockPos pos, Entity entity);

	void dropItems(World world, BlockPos pos, int fortuneLevel);

	void dropItems(World world, BlockPos pos, float luck, int fortuneLevel);

	boolean use(World world, BlockPos pos, PlayerEntity player, InteractionHand hand, Direction face, float faceX, float faceY, float faceZ);

	void startMining(World world, BlockPos pos, PlayerEntity player);

}
