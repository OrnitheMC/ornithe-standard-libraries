package net.ornithemc.osl.commands.api.argument;

import java.util.Objects;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;

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
	public Vec3d getPosition(AbstractCommandSourceStack<?> source) {
		Vec2f vec2f = source.getRotation();
		Vec3d vec3d = source.getAnchor().apply(source);
		float f = MathHelper.cos((vec2f.y + 90.0f) * ((float) Math.PI / 180));
		float g = MathHelper.sin((vec2f.y + 90.0f) * ((float) Math.PI / 180));
		float h = MathHelper.cos(-vec2f.x * ((float) Math.PI / 180));
		float i = MathHelper.sin(-vec2f.x * ((float) Math.PI / 180));
		float j = MathHelper.cos((-vec2f.x + 90.0f) * ((float) Math.PI / 180));
		float k = MathHelper.sin((-vec2f.x + 90.0f) * ((float) Math.PI / 180));
		Vec3d vec3d2 = new Vec3d(f * h, i, g * h);
		Vec3d vec3d3 = new Vec3d(f * j, k, g * j);
		Vec3d vec3d4 = vec3d2.cross(vec3d3).scale(-1.0);
		double d = vec3d2.x * this.forwards + vec3d3.x * this.upwards + vec3d4.x * this.left;
		double e = vec3d2.y * this.forwards + vec3d3.y * this.upwards + vec3d4.y * this.left;
		double l = vec3d2.z * this.forwards + vec3d3.z * this.upwards + vec3d4.z * this.left;
		return new Vec3d(vec3d.x + d, vec3d.y + e, vec3d.z + l);
	}

	@Override
	public Vec2f getRotation(AbstractCommandSourceStack<?> source) {
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
		int i = reader.getCursor();
		double d = LocalCoordinates.parseDouble(reader, i);
		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(i);
			throw Vec3dArgument.INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		reader.skip();
		double e = LocalCoordinates.parseDouble(reader, i);
		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(i);
			throw Vec3dArgument.INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		reader.skip();
		double f = LocalCoordinates.parseDouble(reader, i);
		return new LocalCoordinates(d, e, f);
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
