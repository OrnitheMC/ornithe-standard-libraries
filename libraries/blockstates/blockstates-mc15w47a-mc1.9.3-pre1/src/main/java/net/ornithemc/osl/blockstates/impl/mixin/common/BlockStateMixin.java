package net.ornithemc.osl.blockstates.impl.mixin.common;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
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

@Mixin(BlockState.class)
public interface BlockStateMixin extends BlockStateExtension {

	@Shadow
	Block getBlock();

	@Override
	default boolean is(Block block) {
		return this.getBlock().is(block);
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
	default Box getShape(WorldView world, BlockPos pos) {
		return this.getBlock().getShape((BlockState) this, world, pos);
	}

	@Override
	default boolean canWalkThrough(WorldView world, BlockPos pos) {
		return this.getBlock().canWalkThrough(world, pos);
	}

	@Override
	default boolean canBeReplaced(WorldView world, BlockPos pos) {
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
	default boolean use(World world, BlockPos pos, PlayerEntity player, InteractionHand hand, ItemStack item, Direction face, float faceX, float faceY, float faceZ) {
		return this.getBlock().use(world, pos, (BlockState) this, player, hand, item, face, faceX, faceY, faceZ);
	}

	@Override
	default void startMining(World world, BlockPos pos, PlayerEntity player) {
		this.getBlock().startMining(world, pos, player);
	}
}
