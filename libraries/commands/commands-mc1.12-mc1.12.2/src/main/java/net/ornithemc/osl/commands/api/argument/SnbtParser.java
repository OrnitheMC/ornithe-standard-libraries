package net.ornithemc.osl.commands.api.argument;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.mojang.brigadier.Message;
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
import net.minecraft.text.TranslatableText;

public class SnbtParser {

	public static final SimpleCommandExceptionType TRAILING_DATA_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.nbt.trailing"));
	public static final SimpleCommandExceptionType KEY_EXPECTED_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.nbt.expected.key"));
	public static final SimpleCommandExceptionType VALUE_EXPECTED_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("argument.nbt.expected.value"));
	public static final Dynamic2CommandExceptionType MIXED_LIST_TYPE_EXCEPTION = new Dynamic2CommandExceptionType((type1, type2) -> (Message)new TranslatableText("argument.nbt.list.mixed", type1, type2));
	public static final Dynamic2CommandExceptionType MIXED_ARRAY_TYPE_EXCEPTION = new Dynamic2CommandExceptionType((type1, type2) -> (Message)new TranslatableText("argument.nbt.array.mixed", type1, type2));
	public static final DynamicCommandExceptionType INVALID_ARRAY_EXCEPTION = new DynamicCommandExceptionType(chr -> (Message)new TranslatableText("argument.nbt.array.invalid", chr));

	private static final Pattern DOUBLE_PATTERN_NO_SUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
	private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
	private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
	private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
	private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
	private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
	private static final Pattern PRIMITIVE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");

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
		int i = this.reader.getCursor();
		if (this.reader.peek() == '\"') {
			return new NbtString(this.reader.readQuotedString());
		}
		String string = this.reader.readUnquotedString();
		if (string.isEmpty()) {
			this.reader.setCursor(i);
			throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}
		return this.parseElement(string);
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
				return new NbtByte((byte)1);
			}
			if ("false".equalsIgnoreCase(s)) {
				return new NbtByte((byte)0);
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
		char c = this.reader.peek();
		if (c == '{') {
			return this.readCompound();
		}
		if (c == '[') {
			return this.readListOrArray();
		}
		return this.readElement();
	}

	protected NbtElement readListOrArray() throws CommandSyntaxException {
		if (this.reader.canRead(3) && this.reader.peek(1) != '\"' && this.reader.peek(2) == ';') {
			return this.readArray();
		}
		return this.readList();
	}

	public NbtCompound readCompound() throws CommandSyntaxException {
		this.expect('{');
		NbtCompound nbtCompound = new NbtCompound();
		this.reader.skipWhitespace();
		while (this.reader.canRead() && this.reader.peek() != '}') {
			int i = this.reader.getCursor();
			String string = this.readKey();
			if (string.isEmpty()) {
				this.reader.setCursor(i);
				throw KEY_EXPECTED_EXCEPTION.createWithContext(this.reader);
			}
			this.expect(':');
			nbtCompound.put(string, this.readValue());
			if (!this.hasSeparator())
				break;
			if (this.reader.canRead())
				continue;
			throw KEY_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}
		this.expect('}');
		return nbtCompound;
	}

	private NbtElement readList() throws CommandSyntaxException {
		this.expect('[');
		this.reader.skipWhitespace();
		if (!this.reader.canRead()) {
			throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}
		NbtList nbtList = new NbtList();
		byte i = -1;
		while (this.reader.peek() != ']') {
			int j = this.reader.getCursor();
			NbtElement nbtElement = this.readValue();
			byte k = nbtElement.getType();
			if (i < 0) {
				i = k;
			} else if (k != i) {
				this.reader.setCursor(j);
				throw MIXED_LIST_TYPE_EXCEPTION.createWithContext(this.reader, NbtElement.getName(k), NbtElement.getName(i));
			}
			nbtList.add(nbtElement);
			if (!this.hasSeparator())
				break;
			if (this.reader.canRead())
				continue;
			throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}
		this.expect(']');
		return nbtList;
	}

	private NbtElement readArray() throws CommandSyntaxException {
		this.expect('[');
		int i = this.reader.getCursor();
		char c = this.reader.read();
		this.reader.read();
		this.reader.skipWhitespace();
		if (!this.reader.canRead()) {
			throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}
		if (c == 'B') {
			return new NbtByteArray(this.readArray((byte) 7, (byte) 1));
		}
		if (c == 'L') {
			return new NbtLongArray(this.readArray((byte) 12, (byte) 4));
		}
		if (c == 'I') {
			return new NbtIntArray(this.readArray((byte) 11, (byte) 3));
		}
		this.reader.setCursor(i);
		throw INVALID_ARRAY_EXCEPTION.createWithContext(this.reader, String.valueOf(c));
	}

	private <T extends Number> List<T> readArray(byte arrayType, byte elementType) throws CommandSyntaxException {
		ArrayList<Number> list = new ArrayList<>();
		while (this.reader.peek() != ']') {
			int i = this.reader.getCursor();
			NbtElement nbtElement = this.readValue();
			byte j = nbtElement.getType();
			if (j != elementType) {
				this.reader.setCursor(i);
				throw MIXED_ARRAY_TYPE_EXCEPTION.createWithContext(this.reader, NbtElement.getName(j), NbtElement.getName(arrayType));
			}
			if (elementType == 1) {
				list.add(((NbtByte)nbtElement).getByte());
			} else if (elementType == 4) {
				list.add(((NbtLong)nbtElement).getLong());
			} else {
				list.add(((NbtInt)nbtElement).getInt());
			}
			if (!this.hasSeparator())
				break;
			if (this.reader.canRead())
				continue;
			throw VALUE_EXPECTED_EXCEPTION.createWithContext(this.reader);
		}
		this.expect(']');
		return (List<T>)list;
	}

	private boolean hasSeparator() {
		this.reader.skipWhitespace();
		if (this.reader.canRead() && this.reader.peek() == ',') {
			this.reader.skip();
			this.reader.skipWhitespace();
			return true;
		}
		return false;
	}

	private void expect(char chr) throws CommandSyntaxException {
		this.reader.skipWhitespace();
		this.reader.expect(chr);
	}
}
