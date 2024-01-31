package net.ornithemc.osl.commands.api.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;

public class GlobalCoordinates implements Coordinates {

	private final Coordinate x;
	private final Coordinate y;
	private final Coordinate z;

	public GlobalCoordinates(Coordinate x, Coordinate y, Coordinate z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vec3d getPosition(AbstractCommandSourceStack<?> source) {
		Vec3d pos = source.getPos();
		return new Vec3d(this.x.get(pos.x), this.y.get(pos.y), this.z.get(pos.z));
	}

	@Override
	public Vec2f getRotation(AbstractCommandSourceStack<?> source) {
		Vec2f rot = source.getRotation();
		return new Vec2f((float) this.x.get(rot.x), (float) this.y.get(rot.y));
	}

	@Override
	public boolean isXRelative() {
		return this.x.isRelative();
	}

	@Override
	public boolean isYRelative() {
		return this.y.isRelative();
	}

	@Override
	public boolean isZRelative() {
		return this.z.isRelative();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof GlobalCoordinates)) {
			return false;
		}
		GlobalCoordinates other = (GlobalCoordinates) obj;
		if (!this.x.equals(other.x)) {
			return false;
		}
		if (!this.y.equals(other.y)) {
			return false;
		}
		return this.z.equals(other.z);
	}

	public static GlobalCoordinates parseInt(StringReader reader) throws CommandSyntaxException {
		int i = reader.getCursor();
		Coordinate coordinate = Coordinate.parseInt(reader);
		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(i);
			throw Vec3dArgument.INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		reader.skip();
		Coordinate coordinate2 = Coordinate.parseInt(reader);
		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(i);
			throw Vec3dArgument.INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		reader.skip();
		Coordinate coordinate3 = Coordinate.parseInt(reader);
		return new GlobalCoordinates(coordinate, coordinate2, coordinate3);
	}

	public static GlobalCoordinates parseDouble(StringReader reader, boolean centered) throws CommandSyntaxException {
		int i = reader.getCursor();
		Coordinate coordinate = Coordinate.parseDouble(reader, centered);
		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(i);
			throw Vec3dArgument.INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		reader.skip();
		Coordinate coordinate2 = Coordinate.parseDouble(reader, false);
		if (!reader.canRead() || reader.peek() != ' ') {
			reader.setCursor(i);
			throw Vec3dArgument.INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		reader.skip();
		Coordinate coordinate3 = Coordinate.parseDouble(reader, centered);
		return new GlobalCoordinates(coordinate, coordinate2, coordinate3);
	}

	public static GlobalCoordinates self() {
		return new GlobalCoordinates(new Coordinate(true, 0.0), new Coordinate(true, 0.0), new Coordinate(true, 0.0));
	}

	@Override
	public int hashCode() {
		int hashCode = this.x.hashCode();
		hashCode = 31 * hashCode + this.y.hashCode();
		hashCode = 31 * hashCode + this.z.hashCode();
		return hashCode;
	}
}
