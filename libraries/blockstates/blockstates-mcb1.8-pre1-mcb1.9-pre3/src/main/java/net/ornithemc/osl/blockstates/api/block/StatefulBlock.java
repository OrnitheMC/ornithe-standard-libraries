package net.ornithemc.osl.blockstates.api.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.player.PlayerEntity;
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

	public StatefulBlock(int id, Material material) {
		super(id, material);
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
	public int getSprite(int face, int metadata) {
		return this.getSprite(this.getStateFromMetadata(metadata), Direction.byData3d(face));
	}

	@Override
	public int getSprite(BlockState state, Direction face) {
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
	public float getAmbientOcclusionLight(WorldView world, int x, int y, int z) {
		return this.getAmbientOcclusionLight(world, BlockPos.pooled(x, y, z));
	}

	@Override
	public float getAmbientOcclusionLight(WorldView world, BlockPos pos) {
		return super.getAmbientOcclusionLight(world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public boolean hasSignal(WorldView world, int x, int y, int z, int dir) {
		return this.hasSignal(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), Direction.byData3d(dir));
	}

	@Override
	public boolean hasSignal(WorldView world, BlockPos pos, BlockState state, Direction dir) {
		return super.hasSignal(world, pos.x(), pos.y(), pos.z(), dir.data3d());
	}

	@Override
	@Deprecated
	public boolean hasDirectSignal(World world, int x, int y, int z, int dir) {
		return this.hasDirectSignal(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), Direction.byData3d(dir));
	}

	@Override
	public boolean hasDirectSignal(World world, BlockPos pos, BlockState state, Direction dir) {
		return super.hasDirectSignal(world, pos.x(), pos.y(), pos.z(), dir.data3d());
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
	public void addCollisions(World world, int x, int y, int z, Box shape, ArrayList collisions) {
		this.addCollisions(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), shape, collisions);
	}

	@Override
	public void addCollisions(World world, BlockPos pos, BlockState state, Box shape, List<Box> collisions) {
		super.addCollisions(world, pos.x(), pos.y(), pos.z(), shape, collisions instanceof ArrayList ? (ArrayList<Box>) collisions : new ArrayList<>(collisions));
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
	public boolean canBePlaced(World world, int x, int y, int z) {
		return this.canBePlaced(world, BlockPos.pooled(x, y, z));
	}

	@Override
	public boolean canBePlaced(World world, BlockPos pos) {
		return super.canBePlaced(world, pos.x(), pos.y(), pos.z());
	}

	@Override
	@Deprecated
	public void doEvent(World world, int x, int y, int z, int type, int data) {
		this.doEvent(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), type, data);
	}

	@Override
	public void doEvent(World world, BlockPos pos, BlockState state, int type, int data) {
		super.doEvent(world, pos.x(), pos.y(), pos.z(), type, data);
	}

	@Override
	@Deprecated
	public void neighborChanged(World world, int x, int y, int z, int neighborBlock) {
		this.neighborChanged(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), Block.BY_ID[neighborBlock]);
	}

	@Override
	public void neighborChanged(World world, BlockPos pos, BlockState state, Block neighborBlock) {
		super.neighborChanged(world, pos.x(), pos.y(), pos.z(), neighborBlock.id);
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
	public void onRemoved(World world, int x, int y, int z) {
		this.onRemoved(world, BlockPos.pooled(x, y, z), this.defaultState()); // TODO: is this ok? or should the BlockState arg just be removed?
	}

	@Override
	public void onRemoved(World world, BlockPos pos, BlockState state) {
		super.onRemoved(world, pos.x(), pos.y(), pos.z());
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
	// Block::dropItems(World, int, int, int, int) is final!
	public final void dropItems(World world, BlockPos pos, BlockState state) {
		this.dropItems(world, pos.x(), pos.y(), pos.z(), this.getMetadataFromState(state));
	}

	@Override
	@Deprecated
	public void dropItems(World world, int x, int y, int z, int metadata, float luck) {
		this.dropItems(world, BlockPos.pooled(x, y, z), this.getStateFromMetadata(metadata), luck);
	}

	@Override
	public void dropItems(World world, BlockPos pos, BlockState state, float luck) {
		super.dropItems(world, pos.x(), pos.y(), pos.z(), this.getMetadataFromState(state), luck);
	}

	@Override
	@Deprecated
	public boolean use(World world, int x, int y, int z, PlayerEntity player) {
		return this.use(world, BlockPos.pooled(x, y, z), world.getBlockState(x, y, z), player);
	}

	@Override
	public boolean use(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		return super.use(world, pos.x(), pos.y(), pos.z(), player);
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
