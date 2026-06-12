package net.ornithemc.osl.localization.api.language;

import java.util.Collection;

import net.ornithemc.osl.localization.impl.language.SimpleLanguageMetadata;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;

public interface LanguageMetadata {

	String NAME = "language";
	ResourceMetadata.Section.Serializer<LanguageMetadata> SERIALIZER = SimpleLanguageMetadata.SERIALIZER;
	ResourceMetadata.Section<LanguageMetadata> SECTION = ResourceMetadata.Section.of(NAME, SERIALIZER);

	Collection<Language> getLanguages();

}
