package net.ornithemc.osl.gui.api.color;

import net.ornithemc.osl.gui.impl.color.ColorImpl;

import static net.ornithemc.osl.gui.impl.color.ColorUtil.percentToHexByte;
import static net.ornithemc.osl.gui.impl.color.ColorUtil.putHexByte;

public abstract class Colors {
	public static final double MAX_ALPHA = 1;

	public static final Color WHITE = Colors.hex(0xffffff);
	public static final Color BLACK = Colors.hex(0x000000);

	public static Color rgb(int r, int g, int b) {
		return rgba(r, g, b, MAX_ALPHA);
	}

	public static Color hex(int hex) {
		return hexWithAlpha(hex >> 8 | 0xff);
	}

	public static Color rgba(int r, int g, int b, double a) {
		return hexWithAlpha(
				putHexByte(r, 3) |
						putHexByte(g, 2) |
						putHexByte(b, 1) |
						putHexByte(percentToHexByte(a), 0)
		);
	}

	public static Color hexWithAlpha(int hexWithAlpha) {
		return new ColorImpl(hexWithAlpha);
	}
}
