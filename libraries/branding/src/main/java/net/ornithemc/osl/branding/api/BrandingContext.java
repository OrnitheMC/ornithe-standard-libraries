package net.ornithemc.osl.branding.api;

/**
 * The branding context is the context in which Minecraft branding is displayed.
 * Each context has its own branding modifier, allowing modifications to the
 * branding information to be very general but also very specific.
 */
public enum BrandingContext {

	/**
	 * The title screen that is displayed when after game start-up.
	 */
	TITLE_SCREEN,
	/**
	 * The debug overlay that is displayed in-game upon pressing the F3 key.
	 */
	DEBUG_OVERLAY,
	/**
	 * All of the above.
	 */
	ALL

}
