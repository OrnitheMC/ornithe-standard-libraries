package net.ornithemc.osl.localization.impl;

import java.util.IllegalFormatException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.impl.language.LanguageManager;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;

public final class Localization {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Localization");

	private static LanguageManager languageManager = LanguageManager.instance();
	private static Locale translations = Locale.instance();

	public static LanguageManager getLanguageManager() {
		return languageManager;
	}

	public static Locale getLocale() {
		return translations;
	}

	public static Language getLanguage() {
		return languageManager.getSelectedLanguage();
	}

	public static void setLanguage(Language language) {
		languageManager.setSelectedLanguage(language);
	}

	public static void reloadLanguageManager() {
		languageManager.reload(ResourceManager.client().getResourcePacks());
	}

	public static void reloadLocale() {
		languageManager.reloadLocale(ResourceManager.client());
	}

	public static String get(String key, Object... args) {
		try {
			return String.format(key = translations.get(key), args);
		} catch (IllegalFormatException e) {
			return "format error: " + key;
		}
	}

	public static String getOrDefault(String key, String defaultLocalization) {
		return translations.getOrDefault(key, defaultLocalization);
	}

	public static boolean has(String key) {
		return translations.containsKey(key);
	}
}
