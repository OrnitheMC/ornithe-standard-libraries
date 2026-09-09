package net.ornithemc.osl.blockstates.impl.block.state;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import net.ornithemc.osl.blockstates.api.block.state.BlockStateExtension;

public interface BlockStateExtensionImpl extends BlockStateExtension {

	@Override
	default boolean is(Block block) {
		throw new AbstractMethodError();
	}

	@Override
	default Material getMaterial() {
		throw new AbstractMethodError();
	}

	@Override
	default boolean isOpaque() {
		throw new AbstractMethodError();
	}

	@Override
	default int getOpacity() {
		throw new AbstractMethodError();
	}

	@Override
	default int getLight() {
		throw new AbstractMethodError();
	}

	@Override
	default boolean isTranslucent() {
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
	default boolean usesNeighborLight() {
		throw new AbstractMethodError();
	}

	@Override
	default MapColor getMapColor() {
		throw new AbstractMethodError();
	}

	@Override
	default boolean isCube() {
		throw new AbstractMethodError();
	}

	@Override
	default int getRenderType() {
		throw new AbstractMethodError();
	}

	@Override
	default int getColor() {
		throw new AbstractMethodError();
	}

	@Override
	default int getColorTint(WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default int getLightColor(WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default float getAmbientOcclusionLight() {
		throw new AbstractMethodError();
	}

	@Override
	default boolean blocksAmbientLight() {
		throw new AbstractMethodError();
	}

	@Override
	default boolean isSolid() {
		throw new AbstractMethodError();
	}

	@Override
	default boolean isSignalSource() {
		throw new AbstractMethodError();
	}

	@Override
	default int getSignal(WorldView world, BlockPos pos, Direction dir) {
		throw new AbstractMethodError();
	}

	@Override
	default boolean isAnalogSignalSource() {
		throw new AbstractMethodError();
	}

	@Override
	default int getAnalogSignal(World world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default float getMiningTime(World world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default float getMiningSpeed(PlayerEntity player, World world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default int getDirectSignal(WorldView world, BlockPos pos, Direction dir) {
		throw new AbstractMethodError();
	}

	@Override
	default int getPistonMoveBehavior() {
		throw new AbstractMethodError();
	}

	@Override
	default BlockState resolveVirtualProperties(WorldView world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default BlockState getStateForItemModel() {
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
	default boolean isSolidRender() {
		throw new AbstractMethodError();
	}

	@Override
	default Box getCollisionShape(World world, BlockPos pos) {
		throw new AbstractMethodError();
	}

	@Override
	default void addCollisions(World world, BlockPos pos, Box shape, List<Box> collisions, Entity entity) {
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
	default boolean canBeReplaced(World world, BlockPos pos) {
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
	default boolean use(World world, BlockPos pos, PlayerEntity player, Direction face, float faceX, float faceY, float faceZ) {
		throw new AbstractMethodError();
	}

	@Override
	default void startMining(World world, BlockPos pos, PlayerEntity player) {
		throw new AbstractMethodError();
	}
}
