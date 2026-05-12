package net.ornithemc.osl.localization.impl.language;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.localization.api.language.LanguageMetadata;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;

public class SimpleLanguageMetadata implements LanguageMetadata {

	public static final Serializer SERIALIZER = serializer(SimpleLanguage.FACTORY);

	public static Serializer serializer(Language.Factory languageFactory) {
		return new Serializer(languageFactory);
	}

	private final Collection<Language> languages;

	public SimpleLanguageMetadata(Collection<Language> languages) {
		this.languages = languages;
	}

	@Override
	public Collection<Language> getLanguages() {
		return this.languages;
	}

	public static class Serializer implements ResourceMetadata.Section.Serializer<LanguageMetadata> {

		private static final String REGION = "region";
		private static final String NAME = "name";
		private static final String BIDIRECTIONAL = "bidirectional";

		private static final int LANGUAGE_CODE_MAX_LENGTH = 16;

		private final Language.Factory languageFactory;

		public Serializer(Language.Factory languageFactory) {
			this.languageFactory = languageFactory;
		}

		@Override
		public LanguageMetadata deserialize(JsonObject json) {
			Set<Language> languages = new HashSet<>();

			for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
				String code = entry.getKey();
				JsonElement element = entry.getValue();

				if (code.length() > LANGUAGE_CODE_MAX_LENGTH) {
					throw new JsonParseException("Invalid language code '" + code + "': cannot be more than " + LANGUAGE_CODE_MAX_LENGTH + " characters long");
				}
				if (!element.isJsonObject()) {
					throw new JsonParseException("Invalid language '" + code + "': expected object");
				}

				JsonObject languageJson = element.getAsJsonObject();

				if (!languageJson.has(REGION)) {
					throw new JsonParseException("Invalid language '" + code + "': no region!");
				}
				if (!languageJson.has(NAME)) {
					throw new JsonParseException("Invalid language '" + code + "': no name!");
				}
				
				String region = languageJson.getAsJsonPrimitive(REGION).getAsString();
				String name = languageJson.getAsJsonPrimitive(NAME).getAsString();
				boolean bidirectional = languageJson.has(BIDIRECTIONAL) && languageJson.getAsJsonPrimitive(BIDIRECTIONAL).getAsBoolean();

				if (region.isEmpty()) {
					throw new JsonParseException("Invalid language '" + code + "': region cannot be empty");
				}
				if (name.isEmpty()) {
					throw new JsonParseException("Invalid language '" + code + "' name cannot be empty");
				}

				if (!languages.add(this.languageFactory.create(code, region, name, bidirectional))) {
					throw new JsonParseException("Duplicate language '" + code + "' defined");
				}
			}

			return new SimpleLanguageMetadata(languages);
		}
	}
}
