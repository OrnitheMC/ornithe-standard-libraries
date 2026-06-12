package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;

import net.minecraft.client.resource.metadata.serializer.ResourceMetadataSerializer;
import net.minecraft.resource.SimpleResource;

import net.ornithemc.osl.resource.loader.api.resource.Resource;

class ResourceAdapter extends SimpleResource {

	private final Resource resource;

	ResourceAdapter(Resource resource) throws IOException {
		super(
			resource.sourceName(),
			Adapters.identifier(resource.location()),
			resource.open(),
			null
		);

		this.resource = resource;

		// parse now in case of exceptions (vanilla does not support lazy resources)
		this.resource.metadata();
	}

	@Override
	public <T> T getMetadata(ResourceMetadataSerializer<T> serializer) {
		try {
			return this.resource.metadata().getSection(serializer.getName(), serializer::deserialize);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void close() throws IOException {
		this.resource.close();
	}
}
