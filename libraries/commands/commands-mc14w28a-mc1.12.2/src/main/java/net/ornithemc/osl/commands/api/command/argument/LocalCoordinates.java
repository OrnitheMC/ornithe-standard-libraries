package net.ornithemc.osl.commands.api.command.argument;

import java.util.Objects;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;

public class LocalCoordinates implements Coordinates {

	private final double left;
	private final double upwards;
	private final double forwards;

	public LocalCoordinates(double left, double upwards, double forwards) {
		this.left = left;
		this.upwards = upwards;
		this.forwards = forwards;
	}

	@Override
	public Vec3d getPosition(CommandSourceStack source) {
		Vec2f rotation = source.getRotation();
		Vec3d anchor = source.getAnchor().apply(source);

		float cosY = MathHelper.cos((rotation.y + 90.0F) * ((float) Math.PI / 180));
		float sinY = MathHelper.sin((rotation.y + 90.0f) * ((float) Math.PI / 180));
		float cosX = MathHelper.cos(-rotation.x * ((float) Math.PI / 180));
		float sinX = MathHelper.sin(-rotation.x * ((float) Math.PI / 180));
		float upCosX = MathHelper.cos((-rotation.x + 90.0f) * ((float) Math.PI / 180));
		float upSinX = MathHelper.sin((-rotation.x + 90.0f) * ((float) Math.PI / 180));

		Vec3d forwards = new Vec3d(cosY * cosX, sinX, sinY * cosX);
		Vec3d upwards = new Vec3d(cosY * upCosX, upSinX, sinY * upCosX);
		Vec3d left = forwards.cross(upwards).scale(-1.0);

		return anchor.add(
			forwards.x * this.forwards + upwards.x * this.upwards + left.x * this.left,
			forwards.y * this.forwards + upwards.y * this.upwards + left.y * this.left,
			forwards.z * this.forwards + upwards.z * this.upwards + left.z * this.left
		);
	}

	@Override
	public Vec2f getRotation(CommandSourceStack source) {
		return Vec2f.ZERO;
	}

	@Override
	public boolean isXRelative() {
		return true;
	}

	@Override
	public boolean isYRelative() {
		return true;
	}

	@Override
	public boolean isZRelative() {
		return true;
	}

	public static LocalCoordinates parse(StringReader reader) throws CommandSyntaxException {
		int startCursor = reader.getCursor();

		double x;
		double y;
		double z;

		x = LocalCoordinates.parseDouble(reader, startCursor);

		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(startCursor);
			throw Vec3dArgument.INCOMPLETE_EXCEPTION.createWithContext(reader);
		}

		reader.skip();
		y = LocalCoordinates.parseDouble(reader, startCursor);

		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(startCursor);
			throw Vec3dArgument.INCOMPLETE_EXCEPTION.createWithContext(reader);
		}

		reader.skip();
		z = LocalCoordinates.parseDouble(reader, startCursor);

		return new LocalCoordinates(x, y, z);
	}

	private static double parseDouble(StringReader reader, int pos) throws CommandSyntaxException {
		if (!reader.canRead()) {
			throw Coordinate.NOT_DOUBLE_EXCEPTION.createWithContext(reader);
		}
		if (reader.peek() != '^') {
			reader.setCursor(pos);
			throw Vec3dArgument.MIXED_TYPE_EXCEPTION.createWithContext(reader);
		}
		reader.skip();
		return reader.canRead() && reader.peek() != ' ' ? reader.readDouble() : 0.0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LocalCoordinates)) {
			return false;
		}
		LocalCoordinates other = (LocalCoordinates) obj;
		return this.left == other.left && this.upwards == other.upwards && this.forwards == other.forwards;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.left, this.upwards, this.forwards);
	}
}
