package net.ornithemc.osl.commands.api.command.argument;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.util.JsonUtils;

import net.ornithemc.osl.text.api.TextComponents;

public abstract class NumberRange<T extends Number> {

	public static final SimpleCommandExceptionType EMPTY_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.range.empty"));
	public static final SimpleCommandExceptionType DECIMAL_NOT_ALLOWED_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.range.swapped"));

	protected final T min;
	protected final T max;

	protected NumberRange(T min, T max) {
		this.min = min;
		this.max = max;
	}

	public T getMin() {
		return this.min;
	}

	public T getMax() {
		return this.max;
	}

	public boolean isAny() {
		return this.min == null && this.max == null;
	}

	public JsonElement toJson() {
		if (this.isAny()) {
			return JsonNull.INSTANCE;
		}

		if (this.min != null && this.min.equals(this.max)) {
			return new JsonPrimitive(this.min);
		} else {
			JsonObject json = new JsonObject();

			if (this.min != null) {
				json.addProperty("min", this.min);
			}
			if (this.max != null) {
				json.addProperty("max", this.min);
			}

			return json;
		}
	}

	protected static <T extends Number, R extends NumberRange<T>> R fromJson(JsonElement json, R defaultValue, BiFunction<JsonElement, String, T> parser, NumberRange.Factory<T, R> factory) {
		if (json == null || json.isJsonNull()) {
			return defaultValue;
		} else if (JsonUtils.isNumber(json)) {
			T number = parser.apply(json, "value");
			return factory.create(number, number);
		} else {
			JsonObject valueJson = JsonUtils.asJsonObject(json, "value");

			T min = valueJson.has("min") ? parser.apply(valueJson.get("min"), "min") : null;
			T max = valueJson.has("max") ? parser.apply(valueJson.get("max"), "max") : null;

			return factory.create(min, max);
		}
	}

	protected static <T extends Number, R extends NumberRange<T>> R fromString(StringReader reader, NumberRange.FactoryFromReader<T, R> factory, Function<String, T> parser, Supplier<DynamicCommandExceptionType> exception, Function<T, T> processor) throws CommandSyntaxException {
		if (!reader.canRead()) {
			throw EMPTY_EXCEPTION.createWithContext(reader);
		}

		int startCursor = reader.getCursor();

		try {
			T min;
			T max;

			min = process(valueFromString(reader, parser, exception), processor);

			if (reader.canRead(2) && reader.peek() == '.' && reader.peek(1) == '.') {
				reader.skip();
				reader.skip();

				max = process(valueFromString(reader, parser, exception), processor);
			} else {
				max = min;
			}

			if (min == null && max == null) {
				throw EMPTY_EXCEPTION.createWithContext(reader);
			}

			return factory.create(reader, min, max);
		} catch (CommandSyntaxException e) {
			reader.setCursor(startCursor);
			throw new CommandSyntaxException(e.getType(), e.getRawMessage(), e.getInput(), startCursor);
		}
	}

	private static <T extends Number> T valueFromString(StringReader reader, Function<String, T> parser, Supplier<DynamicCommandExceptionType> exception) throws CommandSyntaxException {
		int startCursor = reader.getCursor();

		while (reader.canRead() && isValidInput(reader)) {
			reader.skip();
		}

		String value = reader.getString().substring(startCursor, reader.getCursor());

		if (value.isEmpty()) {
			return null;
		}

		try {
			return parser.apply(value);
		} catch (NumberFormatException e) {
			throw exception.get().createWithContext(reader, value);
		}
	}

	private static boolean isValidInput(StringReader reader) {
		char chr = reader.peek();

		if (chr >= '0' && chr <= '9') {
			return true;
		}
		if (chr == '-') {
			return true;
		}

		return chr == '.' && (!reader.canRead(2) || reader.peek(1) != '.');
	}

	private static <T> T process(T value, Function<T, T> processor) {
		return value == null ? null : processor.apply(value);
	}

	@FunctionalInterface
	public interface Factory<T extends Number, R extends NumberRange<T>> {

		R create(T min, T max);

	}

	@FunctionalInterface
	public interface FactoryFromReader<T extends Number, R extends NumberRange<T>> {

		R create(StringReader reader, T min, T max) throws CommandSyntaxException;

	}

	public static class Floats extends NumberRange<Float> {

		public static final NumberRange.Floats ANY = new NumberRange.Floats(null, null);

		private static NumberRange.Floats create(StringReader reader, Float min, Float max) throws CommandSyntaxException {
			if (min != null && max != null && min > max) {
				throw DECIMAL_NOT_ALLOWED_EXCEPTION.createWithContext(reader);
			} else {
				return new NumberRange.Floats(min, max);
			}
		}

		private static Double square(Float value) {
			return value == null ? null : value.doubleValue() * value.doubleValue();
		}

		private final Double minSquared;
		private final Double maxSquared;

		private Floats(Float min, Float max) {
			super(min, max);

			this.minSquared = square(min);
			this.maxSquared = square(max);
		}

		public static NumberRange.Floats ofAtLeast(float min) {
			return new NumberRange.Floats(min, null);
		}

		public boolean matches(float value) {
			return this.min != null && this.min > value ? false : this.max == null || !(this.max < value);
		}

		public boolean matchesSquare(double value) {
			return this.minSquared != null && this.minSquared > value ? false : this.maxSquared == null || !(this.maxSquared < value);
		}

		public static NumberRange.Floats fromJson(JsonElement json) {
			return fromJson(json, ANY, JsonUtils::asFloat, NumberRange.Floats::new);
		}

		public static NumberRange.Floats fromString(StringReader reader) throws CommandSyntaxException {
			return fromString(reader, value -> value);
		}

		public static NumberRange.Floats fromString(StringReader reader, Function<Float, Float> processor) throws CommandSyntaxException {
			return fromString(reader, NumberRange.Floats::create, Float::parseFloat, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat, processor);
		}
	}

	public static class Ints extends NumberRange<Integer> {

		public static final NumberRange.Ints ANY = new NumberRange.Ints(null, null);

		private static NumberRange.Ints create(StringReader reader, Integer min, Integer max) throws CommandSyntaxException {
			if (min != null && max != null && min > max) {
				throw DECIMAL_NOT_ALLOWED_EXCEPTION.createWithContext(reader);
			}

			return new NumberRange.Ints(min, max);
		}

		private static Long square(Integer value) {
			return value == null ? null : value.longValue() * value.longValue();
		}

		private final Long minSquared;
		private final Long maxSquared;

		private Ints(Integer min, Integer max) {
			super(min, max);

			this.minSquared = square(min);
			this.maxSquared = square(max);
		}

		public static NumberRange.Ints of(int value) {
			return new NumberRange.Ints(value, value);
		}

		public static NumberRange.Ints ofAtLeast(int min) {
			return new NumberRange.Ints(min, null);
		}

		public boolean matches(int value) {
			return this.min != null && this.min > value ? false : this.max == null || !(this.max < value);
		}

		public boolean matchesSquare(long value) {
			return this.minSquared != null && this.minSquared > value ? false : this.maxSquared == null || !(this.maxSquared < value);
		}

		public static NumberRange.Ints fromJson(JsonElement json) {
			return fromJson(json, ANY, JsonUtils::asInteger, NumberRange.Ints::new);
		}

		public static NumberRange.Ints fromString(StringReader reader) throws CommandSyntaxException {
			return fromString(reader, value -> value);
		}

		public static NumberRange.Ints fromString(StringReader reader, Function<Integer, Integer> processor) throws CommandSyntaxException {
			return fromString(reader, NumberRange.Ints::create, Integer::parseInt, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidInt, processor);
		}
	}
}
