package net.ornithemc.osl.commands.api.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.ornithemc.osl.text.api.TextComponents;

public class Coordinate {

	public static final SimpleCommandExceptionType NOT_DOUBLE_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.pos.missing.double"));
	public static final SimpleCommandExceptionType NOT_INT_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.pos.missing.int"));

	private final boolean relative;
	private final double coordinate;

	public Coordinate(boolean relative, double coordinate) {
		this.relative = relative;
		this.coordinate = coordinate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Coordinate)) {
			return false;
		}

		Coordinate coordinate = (Coordinate) obj;

		if (this.relative != coordinate.relative) {
			return false;
		}

		return Double.compare(coordinate.coordinate, this.coordinate) == 0;
	}

	@Override
	public int hashCode() {
		int parity = this.relative ? 1 : 0;
		long asLong = Double.doubleToLongBits(this.coordinate);

		return 31 * parity + (int) (asLong ^ asLong >>> 32);
	}

	public boolean isRelative() {
		return this.relative;
	}

	public double get(double offset) {
		if (this.relative) {
			return this.coordinate + offset;
		} else {
			return this.coordinate;
		}
	}

	private static boolean parseRelative(StringReader reader) {
		if (reader.peek() == '~') {
			reader.skip();
			return true;
		} else {
			return false;
		}
	}

	public static Coordinate parseDouble(StringReader reader, boolean centered) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '^') {
			throw Vec3dArgument.MIXED_TYPE_EXCEPTION.createWithContext(reader);
		}
		if (!reader.canRead()) {
			throw NOT_DOUBLE_EXCEPTION.createWithContext(reader);
		}

		boolean relative = Coordinate.parseRelative(reader);
		int resetCursor = reader.getCursor();

		double coord = reader.canRead() && reader.peek() != ' ' ? reader.readDouble() : 0.0D;
		String number = reader.getString().substring(resetCursor, reader.getCursor());

		if (relative && number.isEmpty()) {
			return new Coordinate(true, 0.0);
		} else {
			if (!number.contains(".") && !relative && centered) {
				coord += 0.5;
			}

			return new Coordinate(relative, coord);
		}
	}

	public static Coordinate parseInt(StringReader reader) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '^') {
			throw Vec3dArgument.MIXED_TYPE_EXCEPTION.createWithContext(reader);
		}
		if (!reader.canRead()) {
			throw NOT_INT_EXCEPTION.createWithContext(reader);
		}

		boolean relative = Coordinate.parseRelative(reader);
		double coord = reader.canRead() && reader.peek() != ' ' ? (relative ? reader.readDouble() : reader.readInt()) : 0.0D;

		return new Coordinate(relative, coord);
	}
}
