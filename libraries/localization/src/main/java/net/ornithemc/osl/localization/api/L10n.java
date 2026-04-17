package net.ornithemc.osl.localization.api;

import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.impl.Localization;

/**
 * Utility methods for localization.
 */
public final class L10n {

	/**
	 * @return the currently selected language for localization.
	 */
	public static Language getLanguage() {
		return Localization.getLanguage();
	}

	/**
	 * @return the localization of the given translation key.
	 */
	public static String get(String key) {
		return Localization.get(key);
	}

	/**
	 * @return the localization of the given translation key,
	 *         with the given arguments applied.
	 */
	public static String get(String key, Object... args) {
		return Localization.get(key, args);
	}

	/**
	 * @return the localization of the given translation key,
	 *         or the given default value if none exists.
	 */
	public static String getOrDefault(String key, String defaultLocalization) {
		return Localization.getOrDefault(key, defaultLocalization);
	}

	/**
	 * @return whether a localization exists for the given translation key.
	 */
	public static boolean has(String key) {
		return Localization.has(key);
	}
}
