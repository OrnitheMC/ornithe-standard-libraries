package net.ornithemc.osl.blockstates.api.block.state;

import java.util.List;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import net.ornithemc.osl.core.api.util.math.BlockPos;
import net.ornithemc.osl.core.api.util.math.Direction;

public interface BlockProperties {

	Material getMaterial();

	int getOpacity();

	int getLight();

	boolean isTranslucent();

	boolean isAir();

	boolean ticksRandomly();

	boolean updateClients();

	MapColor getMapColor();

	boolean isCube();

	int getRenderType();

	int getColor();

	int getColorTint(WorldView world, BlockPos pos);

	int getSprite(Direction face);

	boolean isSignalSource();

	boolean hasSignal(WorldView world, BlockPos pos, Direction dir);

	float getMiningTime();

	float getMiningSpeed(PlayerEntity player);

	boolean hasDirectSignal(World world, BlockPos pos, Direction dir);

	int getPistonMoveBehavior();

	BlockState resolveVirtualProperties(WorldView world, BlockPos pos);

	Box getOutlineShape(World world, BlockPos pos);

	boolean shouldRenderFace(WorldView world, BlockPos pos, Direction face);

	boolean isSolidRender();

	Box getCollisionShape(World world, BlockPos pos);

	void addCollisions(World world, BlockPos pos, Box shape, List<Box> collisions);

	HitResult rayTrace(World world, BlockPos pos, Vec3d from, Vec3d to);

	boolean canBePlaced(World world, BlockPos pos);

}
