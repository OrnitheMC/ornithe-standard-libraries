package net.ornithemc.osl.text.api;

public enum Formatting {

	BLACK        ('0', 0x000000),
	DARK_BLUE    ('1', 0x0000AA),
	DARK_GREEN   ('2', 0x00AA00),
	DARK_AQUA    ('3', 0x00AAAA),
	DARK_RED     ('4', 0xAA0000),
	DARK_PURPLE  ('5', 0xAA00AA),
	GOLD         ('6', 0xFFAA00),
	GRAY         ('7', 0xAAAAAA),
	DARK_GRAY    ('8', 0x555555),
	BLUE         ('9', 0x5555FF),
	GREEN        ('a', 0x55FF55),
	AQUA         ('b', 0x55FFFF),
	RED          ('c', 0xFF5555),
	LIGHT_PURPLE ('d', 0xFF55FF),
	YELLOW       ('e', 0xFFFF55),
	WHITE        ('f', 0xFFFFFF),
	OBFUSCATED   ('k'),
	BOLD         ('l'),
	STRIKETHROUGH('m'),
	UNDERLINED   ('n'),
	ITALIC       ('o'),
	RESET        ('r');

	public static final char PREFIX = '§';

	final char code;
	final Integer color;

	private Formatting(char code) {
		this(code, null);
	}

	private Formatting(char code, Integer color) {
		this.code = code;
		this.color = color;
	}

	@Override
	public String toString() {
		return "" + PREFIX + this.code;
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

	public static Formatting byCode(char code) {
		for (Formatting f : Formatting.values()) {
			if (f.code == code) {
				return f;
			}
		}

		throw new IllegalStateException("unknown text formatting code " + code);
	}
}
