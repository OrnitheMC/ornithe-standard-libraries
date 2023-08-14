package net.ornithemc.osl.commands.api.argument;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.text.TranslatableText;

public class Coordinate {

	public static final SimpleCommandExceptionType NOT_DOUBLE_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.pos.missing.double", new Object[0]));
	public static final SimpleCommandExceptionType NOT_INT_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.pos.missing.int", new Object[0]));

	private final boolean relative;
	private final double coordinate;

	public Coordinate(boolean relative, double coordinate) {
		this.relative = relative;
		this.coordinate = coordinate;
	}

	public double get(double offset) {
		if (this.relative) {
			return this.coordinate + offset;
		}
		return this.coordinate;
	}

	public static Coordinate parseDouble(StringReader reader, boolean centered) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '^') {
			throw Vec3dArgument.MIXED_TYPE_EXCEPTION.createWithContext(reader);
		}
		if (!reader.canRead()) {
			throw NOT_DOUBLE_EXCEPTION.createWithContext(reader);
		}
		boolean relative = Coordinate.parseRelative(reader);
		int cursor = reader.getCursor();
		double d = reader.canRead() && reader.peek() != ' ' ? reader.readDouble() : 0.0;
		String string = reader.getString().substring(cursor, reader.getCursor());
		if (relative && string.isEmpty()) {
			return new Coordinate(true, 0.0);
		}
		if (!string.contains(".") && !relative && centered) {
			d += 0.5;
		}
		return new Coordinate(relative, d);
	}

	public static Coordinate parseInt(StringReader reader) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '^') {
			throw Vec3dArgument.MIXED_TYPE_EXCEPTION.createWithContext(reader);
		}
		if (!reader.canRead()) {
			throw NOT_INT_EXCEPTION.createWithContext(reader);
		}
		boolean bl = Coordinate.parseRelative(reader);
		double d = reader.canRead() && reader.peek() != ' ' ? (bl ? reader.readDouble() : (double) reader.readInt())
				: 0.0;
		return new Coordinate(bl, d);
	}

	private static boolean parseRelative(StringReader reader) {
		if (reader.peek() == '~') {
			reader.skip();
			return true;
		} else {
			return false;
		}
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
		int i = this.relative ? 1 : 0;
		long l = Double.doubleToLongBits(this.coordinate);
		i = 31 * i + (int) (l ^ l >>> 32);
		return i;
	}

	public boolean isRelative() {
		return this.relative;
	}
}
