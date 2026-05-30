package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.client.resource.metadata.serializer.ResourceMetadataSerializer;
import net.minecraft.resource.Identifier;
import net.minecraft.resource.pack.Pack;
import net.minecraft.resource.pack.PackType;

import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePackFileNotFoundException;

class ResourcePackAdapter implements Pack {

	final ResourcePack pack;

	ResourcePackAdapter(ResourcePack pack) {
		this.pack = pack;
	}

	@Override
	public InputStream getRootResource(String path) throws IOException {
		if (path.contains("/") || path.contains("\\")) {
			throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
		}

		return this.pack.getResource(path);
	}

	@Override
	public InputStream getResource(PackType type, Identifier location) throws IOException {
		IOSupplier<InputStream> resource = this.pack.getResource(Adapters.resourceType(type), location);

		if (resource != null) {
			return resource.get();
		}

		throw new ResourcePackFileNotFoundException(this.pack, ResourcePath.nameOf(Adapters.resourceType(type), location));
	}

	@Override
	public boolean hasResource(PackType type, Identifier location) {
		return this.pack.hasResource(Adapters.resourceType(type), location);
	}

	@Override
	public Collection<Identifier> findResources(PackType type, String directory, int depth, Predicate<String> filter) {
		List<Identifier> locations = new ArrayList<>();

		for (String namespace : this.getNamespaces(type)) {
			this.pack.findResources(Adapters.resourceType(type), namespace, directory, (location, resource) -> {
				if (filter.test(location.identifier())) {
					locations.add(Adapters.identifier(location));
				}
			});
		}

		return locations;
	}

	@Override
	public void close() throws IOException {
		this.pack.close();
	}

	@Override
	public Set<String> getNamespaces(PackType type) {
		return this.pack.getNamespaces(Adapters.resourceType(type));
	}

	@Override
	public <T> T getMetadataSection(ResourceMetadataSerializer<T> serializer) throws IOException {
		return this.pack.getMetadata(serializer.getName(), serializer::deserialize);
	}

	@Override
	public String getName() {
		return this.pack.getName();
	}
}
