package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.io.InputStream;

import com.google.gson.JsonObject;

import net.minecraft.client.resource.metadata.ResourceMetadataSection;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;
import net.ornithemc.osl.resource.loader.impl.resource.JsonResourceMetadata;
import net.ornithemc.osl.resource.loader.impl.resource.ResourceLocationAccess;

class ResourceAdapter implements net.minecraft.client.resource.Resource, ResourceLocationAccess {

	private final ResourceMetadataSerializerRegistry metadataSerializers;
	private final Identifier location;
	private final InputStream resource;
	private final ResourceMetadata metadata;
	private String sourceName;

	ResourceAdapter(ResourceMetadataSerializerRegistry metadataSerializers, Resource resource) throws IOException {
		ResourceMetadata metadata;

		try {
			metadata = resource.metadata();
		} catch (IOException e) {
			metadata = ResourceMetadata.EMPTY;
		}

		this.metadataSerializers = metadataSerializers;
		this.location = Adapters.identifier(resource.location());
		this.resource = resource.open();
		this.metadata = metadata;
		this.sourceName = resource.sourceName();
	}

	@Override
	public Identifier getLocation() {
		return this.location;
	}

	@Override
	public NamespacedIdentifier resourceLocation() {
		return this.location;
	}

	@Override
	public InputStream asStream() {
		return this.resource;
	}

	@Override
	public <T extends ResourceMetadataSection> T getMetadata(String name) {
		if (this.metadata instanceof JsonResourceMetadata) {
			JsonResourceMetadata meta = (JsonResourceMetadata) this.metadata;
			JsonObject metadataJson = meta.asJsonObject();

			return this.metadataSerializers.readMetadata(name, metadataJson);
		} else {
			return null;
		}
	}

	@Override
	public boolean hasMetadata() {
		return this.metadata != ResourceMetadata.EMPTY;
	}

	@Override
	public String getSourceName() {
		return this.sourceName;
	}
}
