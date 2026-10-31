package net.ornithemc.osl.commands.api.command.argument;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;

public interface Coordinates {

	Vec3d getPosition(CommandSourceStack source);

	Vec2f getRotation(CommandSourceStack source);

	default BlockPos getBlockPos(CommandSourceStack source) {
		return new BlockPos(this.getPosition(source));
	}

	boolean isXRelative();

	boolean isYRelative();

	boolean isZRelative();

}
