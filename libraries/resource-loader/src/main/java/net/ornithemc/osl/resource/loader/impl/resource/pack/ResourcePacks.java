package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierException;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourceConsumer;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.resource.JsonResourceMetadata;
import net.ornithemc.osl.text.api.TextComponent;

public final class ResourcePacks {

	// up until 20w45a (1.17 snapshot) Resource Packs
	// and Data Packs used the same format number!
	private static final int SUPPORTED_FORMAT = resolveSupportedPackFormat();

	private static int resolveSupportedPackFormat() {
		MinecraftVersion minecraftVersion = MinecraftVersion.resolve();

		if (minecraftVersion.compareTo("17w48a") >= 0) {
			return 4;
		}
		if (minecraftVersion.compareTo("16w32a") >= 0) {
			return 3;
		}
		if (minecraftVersion.compareTo("15w31a") >= 0) {
			return 2;
		}
		if (minecraftVersion.compareTo("13w26a") >= 0) {
			return 1;
		}

		return 0;
	}

	public static int getSupportedFormat() {
		return SUPPORTED_FORMAT;
	}

	public static List<Path> getRootPaths(Path path, String directory) {
		return getRootPaths(Collections.singleton(path), directory);
	}

	public static List<Path> getRootPaths(Iterable<Path> paths, String directory) {
		List<Path> rootPaths = new ArrayList<>();

		for (Path root : paths) {
			root = root.toAbsolutePath().normalize();

			String separator = root.getFileSystem().getSeparator();
			String dir = directory.replace("/", separator);
			Path path = root.resolve(dir).normalize();

			if (path.startsWith(root) && Files.isDirectory(path)) {
				rootPaths.add(path);
			}
		}

		return rootPaths;
	}

	public static void findNamespaces(ResourcePack pack, Iterable<Path> roots, BiConsumer<ResourceType, String> consumer) {
		for (Path root : roots) {
			findNamespaces(pack, root, consumer);
		}
	}

	public static void findNamespaces(ResourcePack pack, Path root, BiConsumer<ResourceType, String> consumer) {
		String separator = root.getFileSystem().getSeparator();

		for (ResourceType type : ResourceType.values()) {
			Path dir = root.resolve(type.directory());

			if (!Files.isDirectory(dir)) {
				continue;
			}

			try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
				for (Path p : ds) {
					if (!Files.isDirectory(p)) {
						continue;
					}

					String s = p.getFileName().toString();
					String namespace = s.replace(separator, "");

					try {
						consumer.accept(type, NamespacedIdentifiers.validateNamespace(namespace));
					} catch (NamespacedIdentifierException e) {
					}
				}
			} catch (IOException e) {
				ResourceLoader.LOGGER.warn("failed to parse namespaces for resource pack " + pack.getId(), e);
			}
		}
	}

	public static void findResources(ResourcePack pack, Iterable<Path> roots, ResourceType type, String namespace, String directory, ResourceConsumer consumer) {
		for (Path root : roots) {
			findResources(pack, root, type, namespace, directory, consumer);
		}
	}

	public static void findResources(ResourcePack pack, Path root, ResourceType type, String namespace, String directory, ResourceConsumer consumer) {
		String separator = root.getFileSystem().getSeparator();

		Path dir = root.resolve(type.directory()).resolve(namespace);
		Path start = dir.resolve(directory.replace("/", separator));

		if (!Files.isDirectory(start)) {
			return;
		}

		try (Stream<Path> paths = Files.find(
				start,
				Integer.MAX_VALUE,
				(p, attributes) -> attributes.isRegularFile()
								&& (ResourceLoader.DEBUG || !p.toString().endsWith(".ds_store"))
		)) {
			Iterator<Path> it = paths.iterator();

			while (it.hasNext()) {
				Path p = it.next();

				String path = dir.relativize(p).toString().replace(separator, "/");
				NamespacedIdentifier location = NamespacedIdentifiers.from(namespace, path);
				IOSupplier<InputStream> resource = pack.getResource(type, location);

				consumer.accept(location, resource);
			}
		} catch (IOException e) {
			ResourceLoader.LOGGER.debug("error while listing resources from resource pack " + pack.getId(), e);
		}
	}

	public static boolean isMetadataLocation(NamespacedIdentifier location) {
		return location.identifier().endsWith(ResourceMetadata.FILE_EXTENSION);
	}

	public static NamespacedIdentifier getMetadataLocation(NamespacedIdentifier location) {
		return NamespacedIdentifiers.from(location.namespace(), location.identifier() + ResourceMetadata.FILE_EXTENSION);
	}

	public static NamespacedIdentifier getResourceLocation(NamespacedIdentifier metadata) {
		return NamespacedIdentifiers.from(metadata.namespace(), metadata.identifier().substring(0, metadata.identifier().length() - ResourceMetadata.FILE_EXTENSION.length()));
	}

	public static IOSupplier<InputStream> generateMetadataFile(TextComponent description) {
		return JsonResourceMetadata.asInputStream(new SimpleResourcePackMetadata(SUPPORTED_FORMAT, description));
	}
}
