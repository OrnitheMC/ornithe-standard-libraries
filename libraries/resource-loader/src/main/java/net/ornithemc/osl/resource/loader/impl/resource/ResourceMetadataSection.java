package net.ornithemc.osl.resource.loader.impl.resource;

import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata.Section;

public class ResourceMetadataSection<T> implements Section<T> {

	private final String name;
	private final Section.Serializer<T> serializer;

	public ResourceMetadataSection(String name, Section.Serializer<T> serializer) {
		this.name = name;
		this.serializer = serializer;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public Section.Serializer<T> serializer() {
		return this.serializer;
	}
}
