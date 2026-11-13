package net.ornithemc.osl.commands.api.command.argument;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtByteArray;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;

import net.ornithemc.feather.constants.NbtTypes;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.text.api.TextComponents;

public class SnbtParser {

	public static final SimpleCommandExceptionType TRAILING_DATA_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.nbt.trailing"));
	public static final SimpleCommandExceptionType KEY_EXPECTED_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.nbt.expected.key"));
	public static final SimpleCommandExceptionType VALUE_EXPECTED_EXCEPTION = new SimpleCommandExceptionType(TextComponents.translatable("argument.nbt.expected.value"));
	public static final Dynamic2CommandExceptionType MIXED_LIST_TYPE_EXCEPTION = new Dynamic2CommandExceptionType((type1, type2) -> TextComponents.translatable("argument.nbt.list.mixed", type1, type2));
	public static final Dynamic2CommandExceptionType MIXED_ARRAY_TYPE_EXCEPTION = new Dynamic2CommandExceptionType((type1, type2) -> TextComponents.translatable("argument.nbt.array.mixed", type1, type2));
	public static final DynamicCommandExceptionType INVALID_ARRAY_EXCEPTION = new DynamicCommandExceptionType(chr -> TextComponents.translatable("argument.nbt.array.invalid", chr));

	private static final Pattern DOUBLE_PATTERN_NO_SUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
	private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
	private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
	private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
	private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
	private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
	private static final Pattern PRIMITIVE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");

	private static final boolean PARSE_LONG_ARRAY = MinecraftVersion.resolve().compareTo("1.12") >= 0;

	private final StringReader reader;

	public static NbtCompound parse(String rawNbt) throws CommandSyntaxException {
		return new SnbtParser(new StringReader(rawNbt)).parse();
	}

	NbtCompound parse() throws CommandSyntaxException {
		NbtCompound nbt = this.readCompound();
		this.reader.skipWhitespace();

		if (this.reader.canRead()) {
			throw TRAILING_DATA_EXCEPTION.createWithContext(this.reader);
		}

		return nbt;
	}

	public SnbtParser(StringReader reader) {
		this.reader = reader;
	}

	protected String readKey() throws CommandSyntaxException {
		this.reader.skipWhitespace();

		if (!this.reader.canRead()) {
			throw KEY_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}

		return this.reader.readString();
	}

	protected NbtElement readElement() throws CommandSyntaxException {
		this.reader.skipWhitespace();
		int startCursor = this.reader.getCursor();

		if (this.reader.peek() == '\"') {
			return new NbtString(this.reader.readQuotedString());
		}

		String element = this.reader.readUnquotedString();

		if (element.isEmpty()) {
			this.reader.setCursor(startCursor);
			throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}

		return this.parseElement(element);
	}

	private NbtElement parseElement(String s) {
		try {
			if (FLOAT_PATTERN.matcher(s).matches()) {
				return new NbtFloat(Float.parseFloat(s.substring(0, s.length() - 1)));
			}
			if (BYTE_PATTERN.matcher(s).matches()) {
				return new NbtByte(Byte.parseByte(s.substring(0, s.length() - 1)));
			}
			if (LONG_PATTERN.matcher(s).matches()) {
				return new NbtLong(Long.parseLong(s.substring(0, s.length() - 1)));
			}
			if (SHORT_PATTERN.matcher(s).matches()) {
				return new NbtShort(Short.parseShort(s.substring(0, s.length() - 1)));
			}
			if (PRIMITIVE_PATTERN.matcher(s).matches()) {
				return new NbtInt(Integer.parseInt(s));
			}
			if (DOUBLE_PATTERN.matcher(s).matches()) {
				return new NbtDouble(Double.parseDouble(s.substring(0, s.length() - 1)));
			}
			if (DOUBLE_PATTERN_NO_SUFFIX.matcher(s).matches()) {
				return new NbtDouble(Double.parseDouble(s));
			}
			if ("true".equalsIgnoreCase(s)) {
				return new NbtByte((byte) 1);
			}
			if ("false".equalsIgnoreCase(s)) {
				return new NbtByte((byte) 0);
			}
		} catch (NumberFormatException numberFormatException) {
		}

		return new NbtString(s);
	}

	protected NbtElement readValue() throws CommandSyntaxException {
		this.reader.skipWhitespace();

		if (!this.reader.canRead()) {
			throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}

		char chr = this.reader.peek();

		if (chr == '{') {
			return this.readCompound();
		}
		if (chr == '[') {
			return this.readListOrArray();
		}

		return this.readElement();
	}

	protected NbtElement readListOrArray() throws CommandSyntaxException {
		if (this.reader.canRead(3) && this.reader.peek(1) != '\"' && this.reader.peek(2) == ';') {
			return this.readArray();
		} else {
			return this.readList();
		}
	}

	public NbtCompound readCompound() throws CommandSyntaxException {
		this.expect('{');

		NbtCompound compound = new NbtCompound();

		this.reader.skipWhitespace();

		while (this.reader.canRead() && this.reader.peek() != '}') {
			int startCursor = this.reader.getCursor();
			String key = this.readKey();

			if (key.isEmpty()) {
				this.reader.setCursor(startCursor);
				throw KEY_EXPECTED_EXCEPTION.createWithContext(this.reader);
			}

			this.expect(':');
			compound.put(key, this.readValue());

			if (!this.hasSeparator())
				break;
			if (!this.reader.canRead())
				throw KEY_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}

		this.expect('}');

		return compound;
	}

	private NbtElement readList() throws CommandSyntaxException {
		this.expect('[');
		this.reader.skipWhitespace();

		if (!this.reader.canRead()) {
			throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}

		NbtList list = new NbtList();
		byte elementType = -1;

		while (this.reader.peek() != ']') {
			int startCusor = this.reader.getCursor();

			NbtElement element = this.readValue();
			byte type = element.getType();

			if (elementType < 0) {
				elementType = type;
			} else if (type != elementType) {
				this.reader.setCursor(startCusor);
				throw MIXED_LIST_TYPE_EXCEPTION.createWithContext(this.reader, NbtElement.TYPE_NAMES[type], NbtElement.TYPE_NAMES[elementType]);
			}

			list.addElement(element);

			if (!this.hasSeparator())
				break;
			if (!this.reader.canRead())
				throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}

		this.expect(']');

		return list;
	}

	private NbtElement readArray() throws CommandSyntaxException {
		this.expect('[');

		int startCursor = this.reader.getCursor();
		char elementType = this.reader.read();

		this.reader.read();
		this.reader.skipWhitespace();

		if (!this.reader.canRead()) {
			throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}

		if (elementType == 'B') {
			return this.buildByteArray(this.readArray(NbtTypes.BYTE_ARRAY, NbtTypes.BYTE));
		}
		if (elementType == 'I') {
			return this.buildIntArray(this.readArray(NbtTypes.INT_ARRAY, NbtTypes.INT));
		}
		if (PARSE_LONG_ARRAY && elementType == 'L') {
			return this.buildLongArray(this.readArray(NbtTypes.LONG_ARRAY, NbtTypes.LONG));
		}

		this.reader.setCursor(startCursor);
		throw INVALID_ARRAY_EXCEPTION.createWithContext(this.reader, String.valueOf(elementType));
	}

	@SuppressWarnings("unchecked")
	private <T extends Number> List<T> readArray(byte arrayType, byte elementType) throws CommandSyntaxException {
		List<Number> numbers = new ArrayList<>();

		while (this.reader.peek() != ']') {
			int startCursor = this.reader.getCursor();

			NbtElement valueNbt = this.readValue();
			byte valueType = valueNbt.getType();

			if (valueType != elementType) {
				this.reader.setCursor(startCursor);
				throw MIXED_ARRAY_TYPE_EXCEPTION.createWithContext(this.reader, NbtElement.TYPE_NAMES[valueType], NbtElement.TYPE_NAMES[arrayType]);
			}

			if (elementType == NbtTypes.BYTE) {
				numbers.add(((NbtByte) valueNbt).getByte());
			} else if (elementType == NbtTypes.INT) {
				numbers.add(((NbtInt) valueNbt).getInt());
			} else if (elementType == NbtTypes.LONG) {
				numbers.add(((NbtLong) valueNbt).getLong());
			}

			if (!this.hasSeparator())
				break;
			if (!this.reader.canRead())
				throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}

		this.expect(']');

		return (List<T>) numbers;
	}

	private NbtElement buildByteArray(List<Byte> bytes) {
		byte[] array = new byte[bytes.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = bytes.get(i);
		}

		return new NbtByteArray(array);
	}

	private NbtElement buildIntArray(List<Integer> ints) {
		int[] array = new int[ints.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = ints.get(i);
		}

		return new NbtIntArray(array);
	}

	private NbtElement buildLongArray(List<Long> longs) {
		long[] array = new long[longs.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = longs.get(i);
		}

		return new NbtLongArray(array);
	}

	private boolean hasSeparator() {
		this.reader.skipWhitespace();

		if (this.reader.canRead() && this.reader.peek() == ',') {
			this.reader.skip();
			this.reader.skipWhitespace();
			return true;
		} else {
			return false;
		}
	}

	private void expect(char chr) throws CommandSyntaxException {
		this.reader.skipWhitespace();
		this.reader.expect(chr);
	}
}
