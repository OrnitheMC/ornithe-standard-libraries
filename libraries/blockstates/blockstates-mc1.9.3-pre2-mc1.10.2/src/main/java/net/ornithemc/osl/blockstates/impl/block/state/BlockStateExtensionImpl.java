package net.ornithemc.osl.blockstates.impl.block.state;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import net.ornithemc.osl.blockstates.api.block.state.BlockStateExtension;

public interface BlockStateExtensionImpl extends BlockStateExtension {

	@Override
	default boolean is(Block block) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean isAir() {
		throw new AbstractMethodError();
	}

	@Override
	default boolean ticksRandomly() {
		throw new AbstractMethodError();
	}

	@Override
	default Box getShape(WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean canWalkThrough(WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean canBeReplaced(WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean canBePlaced(World world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean doEvent(World world, BlockPos pos, int type, int data) {
		throw new AbstractMethodError();
	}

	@Override
	default void neighborChanged(World world, BlockPos pos, Block neighborBlock) {
		throw new AbstractMethodError();
	}

	@Override
	default void onAdded(World world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default void onRemoved(World world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default void tick(World world, BlockPos pos, Random random) {
		throw new AbstractMethodError();
	}

	@Override
	default void randomTick(World world, BlockPos pos, Random random) {
		throw new AbstractMethodError();
	}

	@Override
	default void onEntityCollision(World world, BlockPos pos, Entity entity) {
		throw new AbstractMethodError();
	}

	@Override
	default void dropItems(World world, BlockPos pos, int fortuneLevel) {
		throw new AbstractMethodError();
	}

	@Override
	default void dropItems(World world, BlockPos pos, float luck, int fortuneLevel) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean use(World world, BlockPos pos, PlayerEntity player, InteractionHand hand, ItemStack item, Direction face, float faceX, float faceY, float faceZ) {
		throw new AbstractMethodError();
	}

	@Override
	default void startMining(World world, BlockPos pos, PlayerEntity player) {
		throw new AbstractMethodError();
	}
}
