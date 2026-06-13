package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.AbstractResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourceConsumer;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePackFileNotFoundException;

public abstract class CompositeResourcePack extends AbstractResourcePack {

	private final List<ResourcePack> resourcePacks = new ArrayList<>();

	public CompositeResourcePack(List<ResourcePack> resourcePacks) {
		this.resourcePacks.addAll(resourcePacks);
	}

	@Override
	public Stream<ResourcePack> flatStream() {
		return this.resourcePacks.stream().map(ResourcePack::flatStream).reduce(Stream.of(), Stream::concat);
	}

	@Override
	public boolean hasResource(String path) {
		for (ResourcePack pack : this.resourcePacks) {
			if (pack.hasResource(path)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		for (ResourcePack pack : this.resourcePacks) {
			if (pack.hasResource(path)) {
				return pack.getResource(path);
			}
		}

		throw new ResourcePackFileNotFoundException(this, path);
	}

	@Override
	public boolean hasResource(ResourceType type, NamespacedIdentifier location) {
		for (ResourcePack pack : this.resourcePacks) {
			if (pack.hasResource(type, location)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public IOSupplier<InputStream> getResource(ResourceType type, NamespacedIdentifier location) {
		for (ResourcePack pack : this.resourcePacks) {
			IOSupplier<InputStream> resource = pack.getResource(type, location);

			if (resource != null) {
				return resource;
			}
		}

		return null;
	}

	@Override
	public void findResources(ResourceType type, String namespace, String directory, ResourceConsumer consumer) {
		for (ResourcePack pack : this.resourcePacks) {
			pack.findResources(type, namespace, directory, consumer);
		}
	}

	@Override
	protected Map<ResourceType, Set<String>> findNamespaces() {
		Map<ResourceType, Set<String>> namespaces = new EnumMap<>(ResourceType.class);

		for (ResourceType type : ResourceType.values()) {
			Set<String> ns = new HashSet<>();

			for (ResourcePack pack : this.resourcePacks) {
				ns.addAll(pack.getNamespaces(type));
			}

			if (!ns.isEmpty()) {
				namespaces.put(type, ns);
			}
		}

		return namespaces;
	}
}
