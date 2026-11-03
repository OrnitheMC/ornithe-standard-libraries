package net.ornithemc.osl.commands.api.command.argument;

import java.util.function.Function;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.ornithemc.osl.text.api.TextComponents;

public class WrappedNumberRange {

	public static final WrappedNumberRange ANY = new WrappedNumberRange(null, null);
	public static final SimpleCommandExceptionType ERROR_INTS_ONLY = new SimpleCommandExceptionType(TextComponents.translatable("argument.range.ints"));

	private final Float min;
	private final Float max;

	public WrappedNumberRange(Float min, Float max) {
		this.min = min;
		this.max = max;
	}

	public Float getMin() {
		return this.min;
	}

	public Float getMax() {
		return this.max;
	}

	public static WrappedNumberRange fromString(StringReader reader, boolean doublePrecision, Function<Float, Float> processor) throws CommandSyntaxException {
		if (!reader.canRead()) {
			throw NumberRange.EMPTY_EXCEPTION.createWithContext(reader);
		}

		int startCursor = reader.getCursor();

		Float min;
		Float max;

		min = process(valueFromString(reader, doublePrecision), processor);

		if (reader.canRead(2) && reader.peek() == '.' && reader.peek(1) == '.') {
			reader.skip();
			reader.skip();

			max = process(valueFromString(reader, doublePrecision), processor);
		} else {
			if (!doublePrecision && reader.canRead() && reader.peek() == '.') {
				reader.setCursor(startCursor);
				throw ERROR_INTS_ONLY.createWithContext(reader);
			}

			max = min;
		}

		if (min == null && max == null) {
			reader.setCursor(startCursor);
			throw NumberRange.EMPTY_EXCEPTION.createWithContext(reader);
		} else {
			return new WrappedNumberRange(min, max);
		}
	}

	private static Float valueFromString(StringReader reader, boolean doublePrecision) throws CommandSyntaxException {
		int startCursor = reader.getCursor();

		while (reader.canRead() && isValidInput(reader, doublePrecision)) {
			reader.skip();
		}

		String value = reader.getString().substring(startCursor, reader.getCursor());

		if (value.isEmpty()) {
			return null;
		}

		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException numberFormatException) {
			if (doublePrecision) {
				throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(reader, value);
			} else {
				throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(reader, value);
			}
		}
	}

	private static boolean isValidInput(StringReader reader, boolean doublePrecision) {
		char chr = reader.peek();

		if (chr >= '0' && chr <= '9') {
			return true;
		}
		if (chr == '-') {
			return true;
		}

		return doublePrecision && chr == '.' && (!reader.canRead(2) || reader.peek(1) != '.');
	}

	private static Float process(Float value, Function<Float, Float> processor) {
		return value == null ? null : (Float) processor.apply(value);
	}
}
