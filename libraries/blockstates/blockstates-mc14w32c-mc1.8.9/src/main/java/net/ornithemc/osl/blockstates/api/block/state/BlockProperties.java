package net.ornithemc.osl.blockstates.api.block.state;

import java.util.List;

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

public interface BlockProperties {

	Material getMaterial();

	boolean isOpaque();

	int getOpacity();

	int getLight();

	boolean isTranslucent();

	boolean isAir();

	boolean ticksRandomly();

	boolean usesNeighborLight();

	MapColor getMapColor();

	boolean isCube();

	int getRenderType();

	int getColor();

	int getColorTint(WorldView world, BlockPos pos);

	int getLightColor(WorldView world, BlockPos pos);

	float getAmbientOcclusionLight();

	boolean blocksAmbientLight();

	boolean isSolid();

	boolean isSignalSource();

	int getSignal(WorldView world, BlockPos pos, Direction dir);

	boolean isAnalogSignalSource();

	int getAnalogSignal(World world, BlockPos pos);

	float getMiningTime(World world, BlockPos pos);

	float getMiningSpeed(PlayerEntity player, World world, BlockPos pos);

	int getDirectSignal(WorldView world, BlockPos pos, Direction dir);

	int getPistonMoveBehavior();

	BlockState resolveVirtualProperties(WorldView world, BlockPos pos);

	BlockState getStateForItemModel();

	Box getOutlineShape(World world, BlockPos pos);

	boolean shouldRenderFace(WorldView world, BlockPos pos, Direction face);

	boolean isSolidRender();

	Box getCollisionShape(World world, BlockPos pos);

	void addCollisions(World world, BlockPos pos, Box shape, List<Box> collisions, Entity entity);

	HitResult rayTrace(World world, BlockPos pos, Vec3d from, Vec3d to);

	boolean canWalkThrough(WorldView world, BlockPos pos);

	boolean canBeReplaced(World world, BlockPos pos);

	boolean canBePlaced(World world, BlockPos pos);

}
