package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.AbstractResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourceConsumer;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePackFileNotFoundException;
import net.ornithemc.osl.resource.loader.impl.resource.LazyResource;

public abstract class PathResourcePack extends AbstractResourcePack {

	private static final FileSystem DEFAULT_FS = FileSystems.getDefault();

	public static boolean fileExists(Path path) {
		return path.getFileSystem() == DEFAULT_FS ? path.toFile().exists() : Files.exists(path);
	}

	private List<Path> rootPaths;

	@Override
	public boolean hasResource(String pathName) {
		Path path = this.getPathToResource(pathName);
		return path != null && fileExists(path);
	}

	@Override
	public InputStream getResource(String pathName) throws IOException {
		Path path = this.getPathToResource(pathName);

		if (path != null && fileExists(path)) {
			return LazyResource.fileInputStream(path);
		}

		throw new ResourcePackFileNotFoundException(this, pathName);
	}

	private Path getPathToResource(String pathName) {
		pathName = ResourcePath.relative(pathName);

		for (Path root : this.getRootPaths()) {
			Path path = ResourcePath.of(root, pathName);

			if (fileExists(path)) {
				return path;
			}
		}

		return null;
	}

	@Override
	protected Map<ResourceType, Set<String>> findNamespaces() {
		Map<ResourceType, Set<String>> namespaces = new EnumMap<>(ResourceType.class);

		ResourcePacks.findNamespaces(this, this.getRootPaths(), (type, namespace) -> {
			namespaces.computeIfAbsent(type, key -> new HashSet<>()).add(namespace);
		});

		return namespaces;
	}

	@Override
	public boolean hasResource(ResourceType type, NamespacedIdentifier location) {
		Path path = this.getPathToResource(type, location);
		return path != null && fileExists(path);
	}

	@Override
	public IOSupplier<InputStream> getResource(ResourceType type, NamespacedIdentifier location) {
		Path path = this.getPathToResource(type, location);

		if (path != null && fileExists(path)) {
			return LazyResource.inputStreamSupplier(path);
		}

		return null;
	}

	private Path getPathToResource(ResourceType type, NamespacedIdentifier location) {
		String pathName = ResourcePath.nameOf(type, location);

		for (Path root : this.getRootPaths()) {
			Path path = ResourcePath.of(root, pathName);

			if (fileExists(path)) {
				return path;
			}
		}

		return null;
	}

	@Override
	public void findResources(ResourceType type, String namespace, String directory, ResourceConsumer consumer) {
		ResourcePacks.findResources(this, this.getRootPaths(), type, namespace, directory, consumer);
	}

	private List<Path> getRootPaths() {
		if (this.rootPaths == null) {
			this.rootPaths = this.findRootPaths();
		}

		return this.rootPaths;
	}

	protected abstract List<Path> findRootPaths();

	@Override
	public void close() {
		super.close();
		this.rootPaths = null;
	}
}
