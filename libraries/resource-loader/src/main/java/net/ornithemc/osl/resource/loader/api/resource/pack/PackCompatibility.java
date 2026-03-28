package net.ornithemc.osl.resource.loader.api.resource.pack;

import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

/**
 * The compatibility status of a resource pack.
 */
public enum PackCompatibility {

	COMPATIBLE, TOO_OLD, TOO_NEW;

	/**
	 * @return a format number representing this compatibility status.
	 */
	public int asFormat() {
		int supportedFormat = ResourcePacks.getSupportedFormat();

		if (this == TOO_OLD) {
			return supportedFormat - 1;
		}
		if (this == TOO_NEW) {
			return supportedFormat + 1;
		}

		return supportedFormat;
	}

	/**
	 * @return the compatibility status corresponding to the given format number.
	 */
	public static PackCompatibility forFormat(int format) {
		int supportedFormat = ResourcePacks.getSupportedFormat();

		if (format < supportedFormat) {
			return TOO_OLD;
		}
		if (format > supportedFormat) {
			return TOO_NEW;
		}

		return COMPATIBLE;
	}
}
