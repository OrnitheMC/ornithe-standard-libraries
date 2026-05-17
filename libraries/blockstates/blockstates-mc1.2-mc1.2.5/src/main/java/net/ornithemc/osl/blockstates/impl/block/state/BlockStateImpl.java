package net.ornithemc.osl.blockstates.impl.block.state;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.state.State;
import net.ornithemc.osl.blockstates.api.state.property.Property;
import net.ornithemc.osl.blockstates.impl.state.AbstractState;
import net.ornithemc.osl.core.api.util.math.BlockPos;
import net.ornithemc.osl.core.api.util.math.Direction;

public class BlockStateImpl extends AbstractState<Block, BlockState> implements BlockState {

	public BlockStateImpl(Block owner, Property<?>[] properties, Comparable<?>[] values, State<Block, BlockState>[][] neighbors) {
		super(owner, properties, values, neighbors);
	}

	@Override
	public Block getBlock() {
		return this.owner;
	}

	@Override
	public boolean is(Block block) {
		return this.owner.is(block);
	}

	@Override
	public Material getMaterial() {
		return this.owner.material;
	}

	@Override
	public int getOpacity() {
		return Block.OPACITIES[this.owner.id];
	}

	@Override
	public int getLight() {
		return Block.LIGHT[this.owner.id];
	}

	@Override
	public boolean isTranslucent() {
		return Block.IS_TRANSLUCENT[this.owner.id];
	}

	@Override
	public boolean isAir() {
		return this.owner.isAir();
	}

	@Override
	public boolean ticksRandomly() {
		return this.owner.ticksRandomly();
	}

	@Override
	public boolean updateClients() {
		return Block.UPDATE_CLIENTS[this.owner.id];
	}

	@Override
	public boolean usesNeighborLight() {
		return Block.USES_NEIGHBOR_LIGHT[this.owner.id];
	}

	@Override
	public MapColor getMapColor() {
		return this.owner.material.color;
	}

	@Override
	public boolean isCube() {
		return this.owner.isCube();
	}

	@Override
	public int getRenderType() {
		return this.owner.getRenderType();
	}

	@Override
	public int getColor() {
		return this.owner.getColor(this);
	}

	@Override
	public int getColorTint(WorldView world, BlockPos pos) {
		return this.owner.getColorTint(world, pos);
	}

	@Override
	public int getSprite(Direction face) {
		return this.owner.getSprite(this, face);
	}

	@Override
	public int getLightColor(WorldView world, BlockPos pos) {
		return this.owner.getLightColor(world, pos);
	}

	@Override
	public float getAmbientOcclusionLight(WorldView world, BlockPos pos) {
		return this.owner.getAmbientOcclusionLight(world, pos);
	}

	@Override
	public boolean isSolid() {
		return Block.isSolid(this.owner.id);
	}

	@Override
	public boolean isSignalSource() {
		return this.owner.isSignalSource();
	}

	@Override
	public boolean hasSignal(WorldView world, BlockPos pos, Direction dir) {
		return this.owner.hasSignal(world, pos, this, dir);
	}

	@Override
	public float getMiningTime() {
		return this.owner.getMiningTime();
	}

	@Override
	public float getMiningSpeed(PlayerEntity player) {
		return this.owner.getMiningSpeed(player);
	}

	@Override
	public boolean hasDirectSignal(World world, BlockPos pos, Direction dir) {
		return this.owner.hasDirectSignal(world, pos, this, dir);
	}

	@Override
	public int getPistonMoveBehavior() {
		return this.owner.getPistonMoveBehavior();
	}

	@Override
	public BlockState resolveVirtualProperties(WorldView world, BlockPos pos) {
		return this.owner.resolveVirtualProperties(this, world, pos);
	}

	@Override
	public Box getOutlineShape(World world, BlockPos pos) {
		return this.owner.getOutlineShape(world, pos);
	}

	@Override
	public boolean shouldRenderFace(WorldView world, BlockPos pos, Direction face) {
		return this.owner.shouldRenderFace(world, pos, face);
	}

	@Override
	public boolean isSolidRender() {
		return this.owner.isSolidRender();
	}

	@Override
	public Box getCollisionShape(World world, BlockPos pos) {
		return this.owner.getCollisionShape(world, pos, this);
	}

	@Override
	public void addCollisions(World world, BlockPos pos, Box shape, List<Box> collisions) {
		this.owner.addCollisions(world, pos, this, shape, collisions);
	}

	@Override
	public HitResult rayTrace(World world, BlockPos pos, Vec3d from, Vec3d to) {
		return this.owner.rayTrace(world, pos, from, to);
	}

	@Override
	public boolean canWalkThrough(WorldView world, BlockPos pos) {
		return this.owner.canWalkThrough(world, pos);
	}

	@Override
	public boolean canBePlaced(World world, BlockPos pos) {
		return this.owner.canBePlaced(world, pos);
	}

	@Override
	public void doEvent(World world, BlockPos pos, int type, int data) {
		this.owner.doEvent(world, pos, this, type, data);
	}

	@Override
	public void neighborChanged(World world, BlockPos pos, Block neighborBlock) {
		this.owner.neighborChanged(world, pos, this, neighborBlock);
	}

	@Override
	public void onAdded(World world, BlockPos pos) {
		this.owner.onAdded(world, pos, this);
	}

	@Override
	public void onRemoved(World world, BlockPos pos) {
		this.owner.onRemoved(world, pos, this);
	}

	@Override
	public void tick(World world, BlockPos pos, Random random) {
		this.owner.tick(world, pos, this, random);
	}

	@Override
	public void onEntityCollision(World world, BlockPos pos, Entity entity) {
		this.owner.onEntityCollision(world, pos, this, entity);
	}

	@Override
	public void dropItems(World world, BlockPos pos, int fortuneLevel) {
		this.owner.dropItems(world, pos, this, fortuneLevel);
	}

	@Override
	public void dropItems(World world, BlockPos pos, float luck, int fortuneLevel) {
		this.owner.dropItems(world, pos, this, luck, fortuneLevel);
	}

	@Override
	public boolean use(World world, BlockPos pos, PlayerEntity player) {
		return this.owner.use(world, pos, this, player);
	}

	@Override
	public void startMining(World world, BlockPos pos, PlayerEntity player) {
		this.owner.startMining(world, pos, player);
	}
}
