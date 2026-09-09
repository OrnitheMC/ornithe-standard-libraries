package net.ornithemc.osl.blockstates.impl.mixin.common;

import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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

@Mixin(BlockState.class)
public interface BlockStateMixin extends BlockStateExtension {

	@Shadow
	Block getBlock();

	@Override
	default boolean is(Block block) {
		return this.getBlock().is(block);
	}

	@Override
	default Material getMaterial() {
		return this.getBlock().getMaterial();
	}

	@Override
	default boolean isOpaque() {
		return this.getBlock().isOpaque();
	}

	@Override
	default int getOpacity() {
		return this.getBlock().getOpacity();
	}

	@Override
	default int getLight() {
		return this.getBlock().getLight();
	}

	@Override
	default boolean isTranslucent() {
		return this.getBlock().isTranslucent();
	}

	@Override
	default boolean isAir() {
		return this.getBlock().isAir();
	}

	@Override
	default boolean ticksRandomly() {
		return this.getBlock().ticksRandomly();
	}

	@Override
	default boolean usesNeighborLight() {
		return this.getBlock().usesNeighborLight();
	}

	@Override
	default MapColor getMapColor() {
		return this.getBlock().getMapColor((BlockState) this);
	}

	@Override
	default boolean isCube() {
		return this.getBlock().isCube();
	}

	@Override
	default int getRenderType() {
		return this.getBlock().getRenderType();
	}

	@Override
	default int getColor() {
		return this.getBlock().getColor((BlockState) this);
	}

	@Override
	default int getColorTint(WorldView world, BlockPos pos) {
		return this.getBlock().getColor(world, pos);
	}

	@Override
	default int getLightColor(WorldView world, BlockPos pos) {
		return this.getBlock().getLightColor(world, pos);
	}

	@Override
	default float getAmbientOcclusionLight() {
		return this.getBlock().getAmbientOcclusionLight();
	}

	@Override
	default boolean blocksAmbientLight() {
		return this.getBlock().blocksAmbientLight();
	}

	@Override
	default boolean isSolid() {
		return this.getBlock().isSolid();
	}

	@Override
	default boolean isSignalSource() {
		return this.getBlock().isSignalSource();
	}

	@Override
	default int getSignal(WorldView world, BlockPos pos, Direction dir) {
		return this.getBlock().getSignal(world, pos, (BlockState) this, dir);
	}

	@Override
	default boolean isAnalogSignalSource() {
		return this.getBlock().isAnalogSignalSource();
	}

	@Override
	default int getAnalogSignal(World world, BlockPos pos) {
		return this.getBlock().getAnalogSignal(world, pos);
	}

	@Override
	default float getMiningTime(World world, BlockPos pos) {
		return this.getBlock().getMiningTime(world, pos);
	}

	@Override
	default float getMiningSpeed(PlayerEntity player, World world, BlockPos pos) {
		return this.getBlock().getMiningSpeed(player, world, pos);
	}

	@Override
	default int getDirectSignal(WorldView world, BlockPos pos, Direction dir) {
		return this.getBlock().getDirectSignal(world, pos, (BlockState) this, dir);
	}

	@Override
	default int getPistonMoveBehavior() {
		return this.getBlock().getPistonMoveBehavior();
	}

	@Override
	default BlockState resolveVirtualProperties(WorldView world, BlockPos pos) {
		return this.getBlock().resolveVirtualProperties((BlockState) this, world, pos);
	}

	@Override
	default BlockState getStateForItemModel() {
		return this.getBlock().getStateForRendering((BlockState) this);
	}

	@Override
	default Box getOutlineShape(World world, BlockPos pos) {
		return this.getBlock().getOutlineShape(world, pos);
	}

	@Override
	default boolean shouldRenderFace(WorldView world, BlockPos pos, Direction face) {
		return this.getBlock().shouldRenderFace(world, pos, face);
	}

	@Override
	default boolean isSolidRender() {
		return this.getBlock().isSolidRender();
	}

	@Override
	default Box getCollisionShape(World world, BlockPos pos) {
		return this.getBlock().getCollisionShape(world, pos, (BlockState) this);
	}

	@Override
	default void addCollisions(World world, BlockPos pos, Box shape, List<Box> collisions, Entity entity) {
		this.getBlock().addCollisions(world, pos, (BlockState) this, shape, collisions, entity);
	}

	@Override
	default HitResult rayTrace(World world, BlockPos pos, Vec3d from, Vec3d to) {
		return this.getBlock().rayTrace(world, pos, from, to);
	}

	@Override
	default boolean canWalkThrough(WorldView world, BlockPos pos) {
		return this.getBlock().canWalkThrough(world, pos);
	}

	@Override
	default boolean canBeReplaced(World world, BlockPos pos) {
		return this.getBlock().canBeReplaced(world, pos);
	}

	@Override
	default boolean canBePlaced(World world, BlockPos pos) {
		return this.getBlock().canBePlaced(world, pos);
	}

	@Override
	default boolean doEvent(World world, BlockPos pos, int type, int data) {
		return this.getBlock().doEvent(world, pos, (BlockState) this, type, data);
	}

	@Override
	default void neighborChanged(World world, BlockPos pos, Block neighborBlock) {
		this.getBlock().neighborChanged(world, pos, (BlockState) this, neighborBlock);
	}

	@Override
	default void onAdded(World world, BlockPos pos) {
		this.getBlock().onAdded(world, pos, (BlockState) this);
	}

	@Override
	default void onRemoved(World world, BlockPos pos) {
		this.getBlock().onRemoved(world, pos, (BlockState) this);
	}

	@Override
	default void tick(World world, BlockPos pos, Random random) {
		this.getBlock().tick(world, pos, (BlockState) this, random);
	}

	@Override
	default void randomTick(World world, BlockPos pos, Random random) {
		this.getBlock().randomTick(world, pos, (BlockState) this, random);
	}

	@Override
	default void onEntityCollision(World world, BlockPos pos, Entity entity) {
		this.getBlock().onEntityCollision(world, pos, (BlockState) this, entity);
	}

	@Override
	default void dropItems(World world, BlockPos pos, int fortuneLevel) {
		this.getBlock().dropItems(world, pos, (BlockState) this, fortuneLevel);
	}

	@Override
	default void dropItems(World world, BlockPos pos, float luck, int fortuneLevel) {
		this.getBlock().dropItems(world, pos, (BlockState) this, luck, fortuneLevel);
	}

	@Override
	default boolean use(World world, BlockPos pos, PlayerEntity player, Direction face, float faceX, float faceY, float faceZ) {
		return this.getBlock().use(world, pos, (BlockState) this, player, face, faceX, faceY, faceZ);
	}

	@Override
	default void startMining(World world, BlockPos pos, PlayerEntity player) {
		this.getBlock().startMining(world, pos, player);
	}
}
