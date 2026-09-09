package net.ornithemc.osl.blockstates.api.block;

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
import net.ornithemc.osl.blockstates.api.state.StateDefinition;
import net.ornithemc.osl.core.api.util.math.BlockPos;
import net.ornithemc.osl.core.api.util.math.Direction;

/**
 * A base implementation of {@linkplain Block} that is designed to be used with {@linkplain BlockState}s.
 * All methods that make use of int coordinates, int directions, and int data values, have their calls
 * forwarded to equivalent methods provided by OSL that make use of {@linkplain BlockPos},
 * {@linkplain Direction}, and {@linkplain BlockState} instead. This allows sub-classes to place all
 * their logic in these methods without the need to duplicate it for interoperability with most Vanilla
 * systems.
 */
public class StatefulBlock extends Block {

	public StatefulBlock(Material material) {
		super(material);
	}

	@Override
	public void buildStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
	}

	@Override
	public BlockState getStateFromMetadata(int metadata) {
		return this.defaultState();
	}

	@Override
	public int getMetadataFromState(BlockState state) {
		return 0;
	}

	@Override
	@Deprecated
	public MapColor getMapColor(int metadata) {
		return this.getMapColor(this.getStateFromMetadata(metadata));
	}

	@Override
	public MapColor getMapColor(BlockState state) {
		return super.getMapColor(this.getMetadataFromState(state));
	}

	@Override
	@Deprecated
	public int getColor(int metadata) {
		return this.getColor(this.getStateFromMetadata(metadata));
	}

	@Override
	public int getColor(BlockState state) {
		return super.getColor(this.getMetadataFromState(state));
	}

	@Override
	@Deprecated
	public int getColor(WorldView world, int x, int y, int z) {
		return this.getColorTint(world, BlockPos.pooled(x, y, z));
	}

	@Override
	public int getColorTint(WorldView world, BlockPos pos) {
		return super.getColor(world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public Sprite getSprite(int face, int metadata) {
		return this.getSprite(this.getStateFromMetadata(metadata), Direction.byData3d(face));
	}

	@Override
	public Sprite getSprite(BlockState state, Direction face) {
		return super.getSprite(face.data3d(), this.getMetadataFromState(state));
	}

	@Override
	@Deprecated
	public int getLightColor(WorldView world, int x, int y, int z) {
		return this.getLightColor(world, BlockPos.pooled(x, y, z));
	}

	@Override
	public int getLightColor(WorldView world, BlockPos pos) {
		return super.getLightColor(world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public int getSignal(WorldView world, int x, int y, int z, int dir) {
		return this.getSignal(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), Direction.byData3d(dir));
	}

	@Override
	public int getSignal(WorldView world, BlockPos pos, BlockState state, Direction dir) {
		return super.getSignal(world, pos.x(), pos.y(), pos.z(), dir.data3d());
	}

	@Override
	@Deprecated
	public int getAnalogSignal(World world, int x, int y, int z, int metadata) {
		return this.getAnalogSignal(world, BlockPos.pooled(x, y, z), this.getStateFromMetadata(metadata));
	}

	@Override
	public int getAnalogSignal(World world, BlockPos pos, BlockState state) {
		return super.getAnalogSignal(world, pos.x(), pos.y(), pos.z(), this.getMetadataFromState(state));
	}

	@Override
	@Deprecated
	public float getMiningTime(World world, int x, int y, int z) {
		return this.getMiningTime(world, BlockPos.pooled(x, y, z));
	}

	@Override
	public float getMiningTime(World world, BlockPos pos) {
		return super.getMiningTime(world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public float getMiningSpeed(PlayerEntity player, World world, int x, int y, int z) {
		return this.getMiningSpeed(player, world, BlockPos.pooled(x, y, z));
	}

	@Override
	public float getMiningSpeed(PlayerEntity player, World world, BlockPos pos) {
		return super.getMiningSpeed(player, world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public int getDirectSignal(WorldView world, int x, int y, int z, int dir) {
		return this.getDirectSignal(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), Direction.byData3d(dir));
	}

	@Override
	public int getDirectSignal(WorldView world, BlockPos pos, BlockState state, Direction dir) {
		return super.getDirectSignal(world, pos.x(), pos.y(), pos.z(), dir.data3d());
	}

	@Override
	@Deprecated
	public Box getOutlineShape(World world, int x, int y, int z) {
		return this.getOutlineShape(world, BlockPos.pooled(x, y, z));
	}

	@Override
	public Box getOutlineShape(World world, BlockPos pos) {
		return super.getOutlineShape(world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public boolean shouldRenderFace(WorldView world, int x, int y, int z, int face) {
		return this.shouldRenderFace(world, BlockPos.pooled(x, y, z), Direction.byData3d(face));
	}

	@Override
	public boolean shouldRenderFace(WorldView world, BlockPos pos, Direction face) {
		return super.shouldRenderFace(world, pos.x(), pos.y(), pos.z(), face.data3d());
	}

	@Override
	@Deprecated
	public Box getCollisionShape(World world, int x, int y, int z) {
		return this.getCollisionShape(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z));
	}

	@Override
	public Box getCollisionShape(World world, BlockPos pos, BlockState state) {
		return super.getCollisionShape(world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public void addCollisions(World world, int x, int y, int z, Box shape, List<Box> collisions, Entity entity) {
		this.addCollisions(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), shape, collisions, entity);
	}

	@Override
	public void addCollisions(World world, BlockPos pos, BlockState state, Box shape, List<Box> collisions, Entity entity) {
		super.addCollisions(world, pos.x(), pos.y(), pos.z(), shape, collisions, entity);
	}

	@Override
	@Deprecated
	public HitResult rayTrace(World world, int x, int y, int z, Vec3d from, Vec3d to) {
		return this.rayTrace(world, BlockPos.pooled(x, y, z), from, to);
	}

	@Override
	public HitResult rayTrace(World world, BlockPos pos, Vec3d from, Vec3d to) {
		return super.rayTrace(world, pos.x(), pos.y(), pos.z(), from, to);
	}

	@Override
	@Deprecated
	public boolean canWalkThrough(WorldView world, int x, int y, int z) {
		return this.canWalkThrough(world, BlockPos.pooled(x, y, z));
	}

	@Override
	public boolean canWalkThrough(WorldView world, BlockPos pos) {
		return super.canWalkThrough(world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public boolean canBePlaced(World world, int x, int y, int z) {
		return this.canBePlaced(world, BlockPos.pooled(x, y, z));
	}

	@Override
	public boolean canBePlaced(World world, BlockPos pos) {
		return super.canBePlaced(world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public boolean doEvent(World world, int x, int y, int z, int type, int data) {
		return this.doEvent(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), type, data);
	}

	@Override
	public boolean doEvent(World world, BlockPos pos, BlockState state, int type, int data) {
		return super.doEvent(world, pos.x(), pos.y(), pos.z(), type, data);
	}

	@Override
	@Deprecated
	public void neighborChanged(World world, int x, int y, int z, Block neighborBlock) {
		this.neighborChanged(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), neighborBlock);
	}

	@Override
	public void neighborChanged(World world, BlockPos pos, BlockState state, Block neighborBlock) {
		super.neighborChanged(world, pos.x(), pos.y(), pos.z(), neighborBlock);
	}

	@Override
	@Deprecated
	public void onAdded(World world, int x, int y, int z) {
		this.onAdded(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z));
	}

	@Override
	public void onAdded(World world, BlockPos pos, BlockState state) {
		super.onAdded(world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public void onRemoved(World world, int x, int y, int z, Block block, int metadata) {
		this.onRemoved(world, BlockPos.pooled(x, y, z), block.getStateFromMetadata(metadata));
	}

	@Override
	public void onRemoved(World world, BlockPos pos, BlockState state) {
		super.onRemoved(world, pos.x(), pos.y(), pos.z(), state.getBlock(), state.getBlock().getMetadataFromState(state));
	}

	@Override
	@Deprecated
	public void tick(World world, int x, int y, int z, Random random) {
		this.tick(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), random);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, Random random) {
		super.tick(world, pos.x(), pos.y(), pos.z(), random);
	}

	@Override
	@Deprecated
	public void onEntityCollision(World world, int x, int y, int z, Entity entity) {
		this.onEntityCollision(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), entity);
	}

	@Override
	public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
		super.onEntityCollision(world, pos.x(), pos.y(), pos.z(), entity);
	}

	@Override
	// Block::dropItems(World, int, int, int, int, int) is final!
	public final void dropItems(World world, BlockPos pos, BlockState state, int fortuneLevel) {
		this.dropItems(world, pos.x(), pos.y(), pos.z(), this.getMetadataFromState(state), fortuneLevel);
	}

	@Override
	@Deprecated
	public void dropItems(World world, int x, int y, int z, int metadata, float luck, int fortuneLevel) {
		this.dropItems(world, BlockPos.pooled(x, y, z), this.getStateFromMetadata(metadata), luck, fortuneLevel);
	}

	@Override
	public void dropItems(World world, BlockPos pos, BlockState state, float luck, int fortuneLevel) {
		super.dropItems(world, pos.x(), pos.y(), pos.z(), this.getMetadataFromState(state), luck, fortuneLevel);
	}

	@Override
	@Deprecated
	public boolean use(World world, int x, int y, int z, PlayerEntity player, int face, float faceX, float faceY, float faceZ) {
		return this.use(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), player, Direction.byData3d(face), faceX, faceY, faceZ);
	}

	@Override
	public boolean use(World world, BlockPos pos, BlockState state, PlayerEntity player, Direction face, float faceX, float faceY, float faceZ) {
		return super.use(world, pos.x(), pos.y(), pos.z(), player, face.data3d(), faceX, faceY, faceZ);
	}

	@Override
	@Deprecated
	public void startMining(World world, int x, int y, int z, PlayerEntity player) {
		this.startMining(world, BlockPos.pooled(x, y, z), player);
	}

	@Override
	public void startMining(World world, BlockPos pos, PlayerEntity player) {
		super.startMining(world, pos.x(), pos.y(), pos.z(), player);
	}
}
