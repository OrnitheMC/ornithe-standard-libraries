package net.ornithemc.osl.blockstates.api.block;

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

import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.blockstates.api.state.StateDefinition;
import net.ornithemc.osl.blockstates.api.state.property.IntegerProperty;
import net.ornithemc.osl.core.api.util.math.BlockPos;
import net.ornithemc.osl.core.api.util.math.Direction;
import net.ornithemc.osl.registries.api.registry.IdMap;

public interface BlockExtension {

	IdMap<BlockState> STATE_REGISTRY = new IdMap<>();

	IntegerProperty DATA_VALUE = IntegerProperty.of("data_value", 0, 15);

	void buildStateDefinition(StateDefinition.Builder<Block, BlockState> builder);

	StateDefinition<Block, BlockState> stateDefinition();

	void setDefaultState(BlockState state);

	BlockState defaultState();

	BlockState getStateFromMetadata(int metadata);

	int getMetadataFromState(BlockState state);

	boolean is(Block block);

	boolean isAir();

	int getColor(BlockState state);

	int getColorTint(WorldView world, BlockPos pos);

	int getSprite(BlockState state, Direction face);

	boolean hasSignal(WorldView world, BlockPos pos, BlockState state, Direction dir);

	boolean hasDirectSignal(World world, BlockPos pos, BlockState state, Direction dir);

	BlockState resolveVirtualProperties(BlockState state, WorldView world, BlockPos pos);

	Box getOutlineShape(World world, BlockPos pos);

	boolean shouldRenderFace(WorldView world, BlockPos pos, Direction face);

	Box getCollisionShape(World world, BlockPos pos, BlockState state);

	void addCollisions(World world, BlockPos pos, BlockState state, Box shape, List<Box> collisions);

	HitResult rayTrace(World world, BlockPos pos, Vec3d from, Vec3d to);

	boolean canBePlaced(World world, BlockPos pos);

	void doEvent(World world, BlockPos pos, BlockState state, int type, int data);

	void neighborChanged(World world, BlockPos pos, BlockState state, Block neighborBlock);

	void onAdded(World world, BlockPos pos, BlockState state);

	void onRemoved(World world, BlockPos pos, BlockState state);

	void tick(World world, BlockPos pos, BlockState state, Random random);

	void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity);

	void dropItems(World world, BlockPos pos, BlockState state);

	void dropItems(World world, BlockPos pos, BlockState state, float luck);

	boolean use(World world, BlockPos pos, BlockState state, PlayerEntity player);

	void startMining(World world, BlockPos pos, PlayerEntity player);

}
