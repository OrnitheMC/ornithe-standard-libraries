package net.ornithemc.osl.branding.api;

import net.ornithemc.osl.branding.impl.BrandingPatchImpl;

public class BrandingPatch {

	/**
	 * Modifies the given branding information for the given context.
	 *
	 * @return the modified string
	 */
	public static String apply(BrandingContext context, String s) {
		return BrandingPatchImpl.apply(context, s);
	}
}
