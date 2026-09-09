package net.ornithemc.osl.blockstates.impl.block.state;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.texture.Sprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
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
		return this.owner.getMaterial();
	}

	@Override
	public boolean isOpaque() {
		return this.owner.isOpaque();
	}

	@Override
	public int getOpacity() {
		return this.owner.getOpacity();
	}

	@Override
	public int getLight() {
		return this.owner.getLight();
	}

	@Override
	public boolean isTranslucent() {
		return this.owner.isTranslucent();
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
	public boolean usesNeighborLight() {
		return this.owner.usesNeighborLight();
	}

	@Override
	public MapColor getMapColor() {
		return this.owner.getMapColor(this);
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
	public Sprite getSprite(Direction face) {
		return this.owner.getSprite(this, face);
	}

	@Override
	public int getLightColor(WorldView world, BlockPos pos) {
		return this.owner.getLightColor(world, pos);
	}

	@Override
	public float getAmbientOcclusionLight() {
		return this.owner.getAmbientOcclusionLight();
	}

	@Override
	public boolean blocksAmbientLight() {
		return this.owner.blocksAmbientLight();
	}

	@Override
	public boolean isSolid() {
		return this.owner.isSolid();
	}

	@Override
	public boolean isSignalSource() {
		return this.owner.isSignalSource();
	}

	@Override
	public int getSignal(WorldView world, BlockPos pos, Direction dir) {
		return this.owner.getSignal(world, pos, this, dir);
	}

	@Override
	public boolean isAnalogSignalSource() {
		return this.owner.isAnalogSignalSource();
	}

	@Override
	public int getAnalogSignal(World world, BlockPos pos) {
		return this.owner.getAnalogSignal(world, pos, this);
	}

	@Override
	public float getMiningTime(World world, BlockPos pos) {
		return this.owner.getMiningTime(world, pos);
	}

	@Override
	public float getMiningSpeed(PlayerEntity player, World world, BlockPos pos) {
		return this.owner.getMiningSpeed(player, world, pos);
	}

	@Override
	public int getDirectSignal(WorldView world, BlockPos pos, Direction dir) {
		return this.owner.getDirectSignal(world, pos, this, dir);
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
	public void addCollisions(World world, BlockPos pos, Box shape, List<Box> collisions, Entity entity) {
		this.owner.addCollisions(world, pos, this, shape, collisions, entity);
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
	public boolean doEvent(World world, BlockPos pos, int type, int data) {
		return this.owner.doEvent(world, pos, this, type, data);
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
	public boolean use(World world, BlockPos pos, PlayerEntity player, Direction face, float faceX, float faceY, float faceZ) {
		return this.owner.use(world, pos, this, player, face, faceX, faceY, faceZ);
	}

	@Override
	public void startMining(World world, BlockPos pos, PlayerEntity player) {
		this.owner.startMining(world, pos, player);
	}
}
