package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Set;

import com.google.gson.JsonObject;

import net.minecraft.client.resource.metadata.serializer.ResourceMetadataSerializer;
import net.minecraft.resource.pack.BuiltInPack;
import net.minecraft.resource.pack.Pack;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata.Section.Serializer;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourceConsumer;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;

public class WrappedPack implements ResourcePack {

	final Pack pack;

	public WrappedPack(Pack pack) {
		this.pack = pack;
	}

	@Override
	public String getId() {
		return this.pack instanceof BuiltInPack ? this.pack.getName() : "resourcepack/" + this.pack.getName();
	}

	@Override
	public String getName() {
		return this.pack.getName();
	}

	@Override
	public boolean hasResource(String path) {
		try {
			return this.getResource(path) != null;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		if (path.contains("/") || path.contains("\\")) {
			return null;
		} else {
			return this.pack.getRootResource(path);
		}
	}

	@Override
	public Set<String> getNamespaces(ResourceType type) {
		return this.pack.getNamespaces(Adapters.packType(type));
	}

	@Override
	public boolean hasResource(ResourceType type, NamespacedIdentifier location) {
		return this.pack.hasResource(Adapters.packType(type), Adapters.identifier(location));
	}

	@Override
	public IOSupplier<InputStream> getResource(ResourceType type, NamespacedIdentifier location) {
		return this.pack.hasResource(Adapters.packType(type), Adapters.identifier(location)) ? () -> this.pack.getResource(Adapters.packType(type), Adapters.identifier(location)) : null;
	}

	@Override
	public void findResources(ResourceType type, String namespace, String directory, ResourceConsumer consumer) {
		this.pack.findResources(Adapters.packType(type), directory, Integer.MAX_VALUE, fileName -> true).forEach(location -> {
			if (location.getNamespace().equals(namespace)) {
				consumer.accept((NamespacedIdentifier) location, () -> this.pack.getResource(Adapters.packType(type), location));
			}
		});
	}

	@Override
	public <T> T getMetadata(String name, Serializer<T> serializer) throws IOException {
		return this.pack.getMetadataSection(new ResourceMetadataSerializer<T>() {

			@Override
			public T deserialize(JsonObject json) {
				return serializer.deserialize(json);
			}

			@Override
			public String getName() {
				return name;
			}
		});
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
		try {
			this.pack.close();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
