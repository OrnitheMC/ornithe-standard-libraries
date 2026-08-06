package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;

import net.minecraft.client.resource.metadata.serializer.ResourceMetadataSerializer;
import net.minecraft.resource.SimpleResource;

import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;

class ResourceAdapter extends SimpleResource {

	private final Resource resource;
	private final ResourceMetadata metadata;

	ResourceAdapter(Resource resource) throws IOException {
		super(
			resource.sourceName(),
			Adapters.identifier(resource.location()),
			resource.open(),
			null
		);

		ResourceMetadata metadata;

		try {
			metadata = resource.metadata();
		} catch (IOException e) {
			metadata = ResourceMetadata.EMPTY;
		}

		this.resource = resource;
		this.metadata = metadata;
	}

	@Override
	public <T> T getMetadata(ResourceMetadataSerializer<T> serializer) {
		return this.metadata.getSection(serializer.getName(), serializer::deserialize);
	}

	@Override
	public boolean m_71215293() {
		return this.metadata != ResourceMetadata.EMPTY;
	}

	@Override
	public void close() throws IOException {
		this.resource.close();
	}
}
