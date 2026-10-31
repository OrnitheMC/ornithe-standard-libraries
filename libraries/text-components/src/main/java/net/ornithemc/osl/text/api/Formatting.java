package net.ornithemc.osl.text.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Formatting {

	BLACK        ("BLACK"        , '0', 0x000000),
	DARK_BLUE    ("DARK_BLUE"    , '1', 0x0000AA),
	DARK_GREEN   ("DARK_GREEN"   , '2', 0x00AA00),
	DARK_AQUA    ("DARK_AQUA"    , '3', 0x00AAAA),
	DARK_RED     ("DARK_RED"     , '4', 0xAA0000),
	DARK_PURPLE  ("DARK_PURPLE"  , '5', 0xAA00AA),
	GOLD         ("GOLD"         , '6', 0xFFAA00),
	GRAY         ("GRAY"         , '7', 0xAAAAAA),
	DARK_GRAY    ("DARK_GRAY"    , '8', 0x555555),
	BLUE         ("BLUE"         , '9', 0x5555FF),
	GREEN        ("GREEN"        , 'a', 0x55FF55),
	AQUA         ("AQUA"         , 'b', 0x55FFFF),
	RED          ("RED"          , 'c', 0xFF5555),
	LIGHT_PURPLE ("LIGHT_PURPLE" , 'd', 0xFF55FF),
	YELLOW       ("YELLOW"       , 'e', 0xFFFF55),
	WHITE        ("WHITE"        , 'f', 0xFFFFFF),
	OBFUSCATED   ("OBFUSCATED"   , 'k'),
	BOLD         ("BOLD"         , 'l'),
	STRIKETHROUGH("STRIKETHROUGH", 'm'),
	UNDERLINED   ("UNDERLINED"   , 'n'),
	ITALIC       ("ITALIC"       , 'o'),
	RESET        ("RESET"        , 'r');

	private static final Map<String, Formatting> BY_NAME = Arrays.stream(values())
		.collect(Collectors.toMap(
			f -> sanitizeName(f.name),
			Function.identity()));

	public static final char PREFIX = '§';

	private static String sanitizeName(String name) {
		return name.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
	}

	final String name;
	final char code;
	final Integer color;

	private Formatting(String name, char code) {
		this(name, code, null);
	}

	private Formatting(String name, char code, Integer color) {
		this.name = name;
		this.code = code;
		this.color = color;
	}

	@Override
	public String toString() {
		return "" + PREFIX + this.code;
	}

	public String getName() {
		return this.name().toLowerCase(Locale.ROOT);
	}

	public char getCode() {
		return this.code;
	}

	public boolean isColor() {
		return this.color != null;
	}

	public Integer getColor() {
		return this.color;
	}

	public static Collection<String> getNames(boolean colors, boolean modifiers) {
		List<String> names = new ArrayList<>();

		for (Formatting formatting : values()) {
			if ((colors || formatting.color == null) && (modifiers || formatting.color != null || formatting != RESET)) {
				names.add(formatting.getName());
			}
		}

		return names;
	}

	public static Formatting byName(String name) {
		return BY_NAME.get(sanitizeName(name));
	}

	public static Formatting byCode(char code) {
		for (Formatting f : Formatting.values()) {
			if (f.code == code) {
				return f;
			}
		}

		throw new IllegalStateException("unknown text formatting code " + code);
	}
}
