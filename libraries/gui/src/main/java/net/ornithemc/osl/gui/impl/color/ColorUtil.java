package net.ornithemc.osl.gui.impl.color;

public class ColorUtil {
	public static int getHexByte(int hexCode, int bytesFromRight) {
		return hexCode >> (8 * bytesFromRight) & 0xff;
	}

	public static int putHexByte(int hexByte, int bytesFromRight) {
		return hexByte << (8 * bytesFromRight);
	}

	public static int percentToHexByte(double percent) {
		return (int) (0xff * percent) & 0xff;
	}

	public static double hexByteToPercent(int hexByte) {
		return (double) hexByte / (double) 0xff;
	}

	public static int replaceHexByte(int hexCode, int newHexByte, int bytesFromRight) {
		return hexCode & ~putHexByte(0xff, bytesFromRight) | putHexByte(newHexByte, bytesFromRight);
	}
}
