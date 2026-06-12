package net.ornithemc.osl.resource.loader.api.resource.pack;

import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;
import net.ornithemc.osl.resource.loader.impl.resource.pack.SimpleResourcePackMetadata;
import net.ornithemc.osl.text.api.TextComponent;

public interface ResourcePackMetadata {

	/**
	 * The name of this pack.mcmeta section.
	 */
	String NAME = "pack";
	/**
	 * The serializer of this pack.mcmeta section.
	 */
	ResourceMetadata.Section.Serializer<ResourcePackMetadata> SERIALIZER = SimpleResourcePackMetadata.SERIALIZER;
	/**
	 * The section type of this pack.mcmeta section.
	 */
	ResourceMetadata.Section<ResourcePackMetadata> SECTION = ResourceMetadata.Section.of(NAME, SERIALIZER);

	/**
	 * @return the format of this resource pack.
	 */
	int format();

	/**
	 * @return the description of this resource pack.
	 */
	TextComponent description();

}
