package net.ornithemc.osl.blockstates.impl.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import net.ornithemc.osl.blockstates.api.block.BlockExtension;
import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.state.StateDefinition;
import net.ornithemc.osl.core.api.util.math.BlockPos;
import net.ornithemc.osl.core.api.util.math.Direction;

public interface BlockExtensionImpl extends BlockExtension {

	@Override
	default void buildStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		throw new AbstractMethodError();
	}

	@Override
	default StateDefinition<Block, BlockState> stateDefinition() {
		throw new AbstractMethodError();
	}

	@Override
	default void setDefaultState(BlockState state) {
		throw new AbstractMethodError();
	}

	@Override
	default BlockState defaultState() {
		throw new AbstractMethodError();
	}

	@Override
	default BlockState getStateFromMetadata(int metadata) {
		throw new AbstractMethodError();
	}

	@Override
	default int getMetadataFromState(BlockState state) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean is(Block block) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean isAir() {
		throw new AbstractMethodError();
	}

	@Override
	default int getColor(BlockState state) {
		throw new AbstractMethodError();
	}

	@Override
	default int getColorTint(WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default int getSprite(BlockState state, Direction face) {
		throw new AbstractMethodError();
	}

	@Override
	default int getLightColor(WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default float getAmbientOcclusionLight(WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean hasSignal(WorldView world, BlockPos pos, BlockState state, Direction dir) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean hasDirectSignal(World world, BlockPos pos, BlockState state, Direction dir) {
		throw new AbstractMethodError();
	}

	@Override
	default BlockState resolveVirtualProperties(BlockState state, WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default Box getOutlineShape(World world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean shouldRenderFace(WorldView world, BlockPos pos, Direction face) {
		throw new AbstractMethodError();
	}

	@Override
	default Box getCollisionShape(World world, BlockPos pos, BlockState state) {
		throw new AbstractMethodError();
	}

	@Override
	default void addCollisions(World world, BlockPos pos, BlockState state, Box shape, List<Box> collisions) {
		throw new AbstractMethodError();
	}

	@Override
	default HitResult rayTrace(World world, BlockPos pos, Vec3d from, Vec3d to) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean canWalkThrough(WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean canBePlaced(World world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default void doEvent(World world, BlockPos pos, BlockState state, int type, int data) {
		throw new AbstractMethodError();
	}

	@Override
	default void neighborChanged(World world, BlockPos pos, BlockState state, Block neighborBlock) {
		throw new AbstractMethodError();
	}

	@Override
	default void onAdded(World world, BlockPos pos, BlockState state) {
		throw new AbstractMethodError();
	}

	@Override
	default void onRemoved(World world, BlockPos pos, BlockState state) {
		throw new AbstractMethodError();
	}

	@Override
	default void tick(World world, BlockPos pos, BlockState state, Random random) {
		throw new AbstractMethodError();
	}

	@Override
	default void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
		throw new AbstractMethodError();
	}

	@Override
	default void dropItems(World world, BlockPos pos, BlockState state, int fortuneLevel) {
		throw new AbstractMethodError();
	}

	@Override
	default void dropItems(World world, BlockPos pos, BlockState state, float luck, int fortuneLevel) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean use(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		throw new AbstractMethodError();
	}

	@Override
	default void startMining(World world, BlockPos pos, PlayerEntity player) {
		throw new AbstractMethodError();
	}
}
