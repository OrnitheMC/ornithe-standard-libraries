package net.ornithemc.osl.resource.loader.impl.resource.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.JsonResourceMetadata;
import net.ornithemc.osl.resource.loader.impl.resource.LazyResource;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

public class FallbackResourceManager implements ResourceManager {

	private final ResourceType type;
	private final String namespace;
	private final List<ResourcePack> fallbacks;

	public FallbackResourceManager(ResourceType type, String namespace) {
		this.type = type;
		this.namespace = namespace;
		this.fallbacks = new ArrayList<>();
	}

	public void add(ResourcePack pack) {
		this.fallbacks.add(pack);
	}

	@Override
	public boolean hasResource(String path) {
		throw new UnsupportedOperationException("Use LegacyResourceManager!");
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		throw new UnsupportedOperationException("Use LegacyResourceManager!");
	}

	@Override
	public List<InputStream> getResourceStack(String path) throws IOException {
		throw new UnsupportedOperationException("Use LegacyResourceManager!");
	}

	@Override
	public Set<String> getNamespaces() {
		return Collections.singleton(this.namespace);
	}

	@Override
	public boolean hasResource(NamespacedIdentifier location) {
		for (int i = this.fallbacks.size() - 1; i >= 0; i--) {
			ResourcePack fallback = this.fallbacks.get(i);

			if (fallback.hasResource(this.type, location)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Optional<Resource> getResource(NamespacedIdentifier location) {
		for (int i = this.fallbacks.size() - 1; i >= 0; i--) {
			ResourcePack fallback = this.fallbacks.get(i);
			IOSupplier<InputStream> resource = fallback.getResource(this.type, location);

			if (resource != null) {
				IOSupplier<ResourceMetadata> metadata = this.findResourceMetadata(location, i);
				return Optional.of(new LazyResource(fallback.getId(), location, resource, metadata));
			}
		}

		return Optional.empty();
	}

	private IOSupplier<ResourceMetadata> findResourceMetadata(NamespacedIdentifier resourceLocation, int resourceIndex) {
		return () -> {
			NamespacedIdentifier location = ResourcePacks.getMetadataLocation(resourceLocation);

			for (int i = this.fallbacks.size() - 1; i >= resourceIndex; i--) {
				ResourcePack fallback = this.fallbacks.get(i);
				IOSupplier<InputStream> metadata = fallback.getResource(this.type, location);

				if (metadata != null) {
					return JsonResourceMetadata.fromInputStream(metadata);
				}
			}

			return ResourceMetadata.EMPTY;
		};
	}

	@Override
	public List<Resource> getResourceStack(NamespacedIdentifier location) {
		List<Resource> resources = new ArrayList<>();

		for (int i = 0; i < this.fallbacks.size(); i++) {
			ResourcePack fallback = this.fallbacks.get(i);
			IOSupplier<InputStream> resource = fallback.getResource(this.type, location);

			if (resource != null) {
				IOSupplier<ResourceMetadata> metadata = this.getResourceMetadata(fallback, location);
				resources.add(new LazyResource(fallback.getId(), location, resource, metadata));
			}
		}

		return resources;
	}

	private IOSupplier<ResourceMetadata> getResourceMetadata(ResourcePack fallback, NamespacedIdentifier resourceLocation) {
		return () -> {
			NamespacedIdentifier location = ResourcePacks.getMetadataLocation(resourceLocation);
			IOSupplier<InputStream> metadata = fallback.getResource(this.type, location);

			if (metadata != null) {
				return JsonResourceMetadata.fromInputStream(metadata);
			}

			return ResourceMetadata.EMPTY;
		};
	}

	@Override
	public Map<NamespacedIdentifier, Resource> findResources(String directory, Predicate<NamespacedIdentifier> filter) {
		Map<NamespacedIdentifier, Resource> resources = new TreeMap<>(NamespacedIdentifiers.COMPARATOR);

		Map<NamespacedIdentifier, ResourceFromFallback> resourceSuppliers = new HashMap<>();
		Map<NamespacedIdentifier, ResourceFromFallback> metadataSuppliers = new HashMap<>();

		for (int i = 0; i < this.fallbacks.size(); i++) {
			int fallbackIndex = i;
			ResourcePack fallback = this.fallbacks.get(i);

			fallback.findResources(this.type, this.namespace, directory, (location, resource) -> {
				if (ResourcePacks.isMetadataLocation(location)) {
					NamespacedIdentifier resourceLocation = ResourcePacks.getResourceLocation(location);

					if (filter.test(resourceLocation)) {
						metadataSuppliers.put(resourceLocation, new ResourceFromFallback(fallbackIndex, fallback.getId(), resource));
					}
				} else {
					if (filter.test(location)) {
						resourceSuppliers.put(location, new ResourceFromFallback(fallbackIndex, fallback.getId(), resource));
					}
				}
			});
		}

		resourceSuppliers.forEach((location, resourceFromFallback) -> {
			ResourceFromFallback metadataFromFallback = metadataSuppliers.get(location);
			IOSupplier<InputStream> resource = resourceFromFallback.resource;
			IOSupplier<ResourceMetadata> metadata;

			if (metadataFromFallback != null && metadataFromFallback.index >= resourceFromFallback.index) {
				metadata = ResourceMetadata.supplier(metadataFromFallback.resource);
			} else {
				metadata = ResourceMetadata.EMPTY_SUPPLIER;
			}

			resources.put(location, new LazyResource(resourceFromFallback.name, location, resource, metadata));
		});

		return resources;
	}

	@Override
	public Map<NamespacedIdentifier, Resource> findResources(String namespace, String directory, Predicate<NamespacedIdentifier> filter) {
		throw new UnsupportedOperationException("Use SimpleReloadableResourceManager!");
	}

	@Override
	public Map<NamespacedIdentifier, List<Resource>> findResourceStacks(String directory, Predicate<NamespacedIdentifier> filter) {
		Map<NamespacedIdentifier, List<Resource>> resources = new TreeMap<>(NamespacedIdentifiers.COMPARATOR);

		Map<NamespacedIdentifier, Map<String, IOSupplier<InputStream>>> resourceSuppliers = new HashMap<>();
		Map<NamespacedIdentifier, Map<String, IOSupplier<InputStream>>> metadataSuppliers = new HashMap<>();

		for (int i = 0; i < this.fallbacks.size(); i++) {
			ResourcePack fallback = this.fallbacks.get(i);

			fallback.findResources(this.type, this.namespace, directory, (location, resource) -> {
				if (ResourcePacks.isMetadataLocation(location)) {
					NamespacedIdentifier resourceLocation = ResourcePacks.getResourceLocation(location);

					if (filter.test(resourceLocation)) {
						metadataSuppliers.computeIfAbsent(resourceLocation, key -> new LinkedHashMap<>()).put(fallback.getId(), resource);
					}
				} else {
					if (filter.test(location)) {
						resourceSuppliers.computeIfAbsent(location, key -> new LinkedHashMap<>()).put(fallback.getId(), resource);
					}
				}
			});
		}

		resourceSuppliers.forEach((location, resourceStack) -> {
			Map<String, IOSupplier<InputStream>> metadataStack = metadataSuppliers.get(location);

			for (Map.Entry<String, IOSupplier<InputStream>> e : resourceStack.entrySet()) {
				String fallbackId = e.getKey();
				IOSupplier<InputStream> resource = e.getValue();
				IOSupplier<ResourceMetadata> metadata;

				if (metadataStack != null && metadataStack.containsKey(fallbackId)) {
					metadata = ResourceMetadata.supplier(metadataStack.get(fallbackId));
				} else {
					metadata = ResourceMetadata.EMPTY_SUPPLIER;
				}

				resources.computeIfAbsent(location, key -> new ArrayList<>()).add(new LazyResource(fallbackId, location, resource, metadata));
			}
		});

		return resources;
	}

	@Override
	public Map<NamespacedIdentifier, List<Resource>> findResourceStacks(String namespace, String directory, Predicate<NamespacedIdentifier> filter) {
		throw new UnsupportedOperationException("Use SimpleReloadableResourceManager!");
	}

	private static class ResourceFromFallback {

		public final int index;
		public final String name;
		public final IOSupplier<InputStream> resource;

		public ResourceFromFallback(int index, String name, IOSupplier<InputStream> resource) {
			this.index = index;
			this.name = name;
			this.resource = resource;
		}
	}
}
