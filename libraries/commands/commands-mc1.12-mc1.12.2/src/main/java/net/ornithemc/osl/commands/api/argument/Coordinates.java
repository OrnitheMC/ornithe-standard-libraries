package net.ornithemc.osl.commands.api.argument;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;

public interface Coordinates {

	Vec3d getPosition(AbstractCommandSourceStack<?> source);

	Vec2f getRotation(AbstractCommandSourceStack<?> source);

	default BlockPos getBlockPos(AbstractCommandSourceStack<?> source) {
		return new BlockPos(this.getPosition(source));
	}

	boolean isXRelative();

	boolean isYRelative();

	boolean isZRelative();

}
