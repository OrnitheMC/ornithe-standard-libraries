package net.ornithemc.osl.blocks.impl.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class AirBlock extends Block {

	public AirBlock() {
		super(Material.AIR);
	}

	@Override
	public boolean isAir() {
		return true;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public Box getCollisionShape(World world, int x, int y, int z) {
		return null;
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean canRayTrace(int metadata, boolean allowLiquids) {
		return false;
	}

	@Override
	public void dropItems(World world, int x, int y, int z, int metadata, float luck, int fortuneLevel) {
	}
}
