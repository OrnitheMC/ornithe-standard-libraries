package net.ornithemc.osl.gui.impl.color;

import net.ornithemc.osl.gui.api.color.Color;

import static net.ornithemc.osl.gui.impl.color.ColorUtil.*;

public class ColorImpl implements Color {
	private final int hexWithAlpha;

	public ColorImpl(int hexWithAlpha) {
		this.hexWithAlpha = hexWithAlpha;
	}

	@Override
	public int red() {
		return getHexByte(this.hexWithAlpha, 3);
	}

	@Override
	public int green() {
		return getHexByte(this.hexWithAlpha, 2);
	}

	@Override
	public int blue() {
		return getHexByte(this.hexWithAlpha,1);
	}

	@Override
	public double alpha() {
		return hexByteToPercent(getHexByte(this.hexWithAlpha,0));
	}

	@Override
	public int hexNoAlpha() {
		return this.hexWithAlpha >> 8;
	}

	@Override
	public int hexWithAlpha() {
		return this.hexWithAlpha;
	}

	@Override
	public Color withRed(int r) {
		return new ColorImpl(replaceHexByte(this.hexWithAlpha, r, 3));
	}

	@Override
	public Color withGreen(int g) {
		return new ColorImpl(replaceHexByte(this.hexWithAlpha, g, 2));
	}

	@Override
	public Color withBlue(int b) {
		return new ColorImpl(replaceHexByte(this.hexWithAlpha, b, 1));
	}

	@Override
	public Color withAlpha(double a) {
		return new ColorImpl(replaceHexByte(this.hexWithAlpha, percentToHexByte(a), 0));
	}
}
