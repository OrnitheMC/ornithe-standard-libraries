package net.ornithemc.osl.blockstates.api.block.state;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

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

	@Environment(EnvType.CLIENT)
	boolean isTranslucent();

	boolean isAir();

	boolean ticksRandomly();

	boolean usesNeighborLight();

	MapColor getMapColor();

	boolean isCube();

	int getRenderType();

	@Environment(EnvType.CLIENT)
	int getColor();

	@Environment(EnvType.CLIENT)
	int getColorTint(WorldView world, BlockPos pos);

	@Environment(EnvType.CLIENT)
	int getLightColor(WorldView world, BlockPos pos);

	@Environment(EnvType.CLIENT)
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

	@Environment(EnvType.CLIENT)
	BlockState getStateForItemModel();

	@Environment(EnvType.CLIENT)
	Box getOutlineShape(World world, BlockPos pos);

	@Environment(EnvType.CLIENT)
	boolean shouldRenderFace(WorldView world, BlockPos pos, Direction face);

	boolean isSolidRender();

	Box getCollisionShape(World world, BlockPos pos);

	void addCollisions(World world, BlockPos pos, Box shape, List<Box> collisions, Entity entity);

	HitResult rayTrace(World world, BlockPos pos, Vec3d from, Vec3d to);

	boolean canWalkThrough(WorldView world, BlockPos pos);

	boolean canBeReplaced(World world, BlockPos pos);

	boolean canBePlaced(World world, BlockPos pos);

}
