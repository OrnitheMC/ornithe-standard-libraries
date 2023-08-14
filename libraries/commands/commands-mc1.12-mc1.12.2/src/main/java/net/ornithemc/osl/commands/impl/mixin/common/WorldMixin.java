package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.ornithemc.osl.commands.impl.interfaces.mixin.IWorld;

@Mixin(World.class)
public class WorldMixin implements IWorld {

	@Shadow private boolean contains(BlockPos pos) { return false; }

	@Override
	public boolean osl$commands$contains(BlockPos pos) {
		return contains(pos);
	}

}
