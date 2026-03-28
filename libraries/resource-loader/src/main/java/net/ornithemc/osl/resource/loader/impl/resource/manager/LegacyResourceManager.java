package net.ornithemc.osl.resource.loader.impl.resource.manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;

public class LegacyResourceManager implements ResourceManager {

	private final List<ResourcePack> fallbacks;

	public LegacyResourceManager() {
		this.fallbacks = new ArrayList<>();
	}

	public void add(ResourcePack pack) {
		this.fallbacks.add(pack);
	}

	@Override
	public List<ResourcePack> getResourcePacks() {
		return Collections.unmodifiableList(this.fallbacks);
	}

	@Override
	public boolean hasResource(String path) {
		for (int i = this.fallbacks.size() - 1; i >= 0; i--) {
			ResourcePack fallback = this.fallbacks.get(i);

			if (fallback.hasResource(path)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		for (int i = this.fallbacks.size() - 1; i >= 0; i--) {
			ResourcePack fallback = this.fallbacks.get(i);

			if (fallback.hasResource(path)) {
				return fallback.getResource(path);
			}
		}

		throw new FileNotFoundException(path);
	}

	@Override
	public List<InputStream> getResourceStack(String path) throws IOException {
		List<InputStream> resources = new ArrayList<>();

		for (int i = 0; i < this.fallbacks.size(); i++) {
			ResourcePack fallback = this.fallbacks.get(i);

			if (fallback.hasResource(path)) {
				resources.add(fallback.getResource(path));
			}
		}

		if (!resources.isEmpty()) {
			return resources;
		}

		throw new FileNotFoundException(path);
	}

	@Override
	public Set<String> getNamespaces() {
		throw new UnsupportedOperationException("Use FallbackResourceManager!");
	}

	@Override
	public boolean hasResource(NamespacedIdentifier location) {
		throw new UnsupportedOperationException("Use FallbackResourceManager!");
	}

	@Override
	public Optional<Resource> getResource(NamespacedIdentifier location) {
		throw new UnsupportedOperationException("Use FallbackResourceManager!");
	}

	@Override
	public List<Resource> getResourceStack(NamespacedIdentifier location) {
		throw new UnsupportedOperationException("Use FallbackResourceManager!");
	}

	@Override
	public Map<NamespacedIdentifier, Resource> findResources(String directory, Predicate<NamespacedIdentifier> filter) {
		throw new UnsupportedOperationException("Use FallbackResourceManager!");
	}

	@Override
	public Map<NamespacedIdentifier, Resource> findResources(String namespace, String directory, Predicate<NamespacedIdentifier> filter) {
		throw new UnsupportedOperationException("Use SimpleReloadableResourceManager!");
	}

	@Override
	public Map<NamespacedIdentifier, List<Resource>> findResourceStacks(String directory, Predicate<NamespacedIdentifier> filter) {
		throw new UnsupportedOperationException("Use FallbackResourceManager!");
	}

	@Override
	public Map<NamespacedIdentifier, List<Resource>> findResourceStacks(String namespace, String directory, Predicate<NamespacedIdentifier> filter) {
		throw new UnsupportedOperationException("Use SimpleReloadableResourceManager!");
	}

	public void clear() {
		this.fallbacks.clear();
	}
}
