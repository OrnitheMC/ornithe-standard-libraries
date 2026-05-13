package net.ornithemc.osl.localization.impl.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.api.language.LanguageMetadata;
import net.ornithemc.osl.localization.impl.Locale;
import net.ornithemc.osl.localization.impl.Localization;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

public final class LanguageManager {

	private static final LanguageManager INSTANCE = new LanguageManager();

	public static final LanguageManager instance() {
		return INSTANCE;
	}

	public static final String EN_US = ResourcePacks.getSupportedFormat() < 3 ? "en_US" : "en_us";
	public static final String DEFAULT_LANGUAGE = EN_US;
	public static final String FALLBACK_LANGUAGE = EN_US;

	private final Locale locale = Locale.instance();

	private boolean wrapper;
	private Map<String, Language> languages;
	private ResourceMetadata.Section<LanguageMetadata> metadataSection;

	private String selectedLanguage;

	private LanguageManager() {
		this.languages = new HashMap<>();
		this.metadataSection = LanguageMetadata.SECTION;

		this.selectedLanguage = DEFAULT_LANGUAGE;
	}

	public void wrap(Map<String, Language> map, Language.Factory languageFactory) {
		if (this.wrapper) {
			throw new IllegalStateException("Attempted to initialize LanguageManager multiple times!");
		} else {
			ResourceMetadata.Section.Serializer<LanguageMetadata> serializer = SimpleLanguageMetadata.serializer(languageFactory);

			this.languages = map;
			this.metadataSection = ResourceMetadata.Section.of(LanguageMetadata.NAME, serializer);
		}
	}

	public Language getSelectedLanguage() {
		String code = this.selectedLanguage;
		if (!this.languages.containsKey(code)) {
			code = DEFAULT_LANGUAGE;
		}

		return this.languages.get(code);
	}

	public void setSelectedLanguage(Language language) {
		this.selectedLanguage = language.code();
	}

	public void setSelectedLanguage(String language) {
		this.selectedLanguage = language;
	}

	public SortedSet<Language> getLanguages() {
		return new TreeSet<>(this.languages.values());
	}

	public Language getLanguage(String code) {
		return this.languages.get(code);
	}

	public void reload(List<ResourcePack> resourcePacks) {
		this.languages.clear();

		for (ResourcePack resourcePack : resourcePacks) {
			try {
				LanguageMetadata metadata = resourcePack.getMetadata(this.metadataSection);

				if (metadata != null) {
					for (Language language : metadata.getLanguages()) {
						this.languages.putIfAbsent(language.code(), language);
					}
				}
			} catch (Exception e) {
				Localization.LOGGER.warn("Unable to parse language metadata from resource pack: {}", resourcePack.getId(), e);
			}
		}
	}

	public void reloadLocale(ResourceManager resourceManager) {
		List<String> languageCodes = new ArrayList<>();

		if (!FALLBACK_LANGUAGE.equals(this.selectedLanguage)) {
			languageCodes.add(FALLBACK_LANGUAGE);
		}
		languageCodes.add(this.selectedLanguage);

		this.locale.reload(resourceManager, languageCodes);
	}
}
