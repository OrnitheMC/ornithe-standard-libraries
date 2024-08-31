package net.ornithemc.osl.resource.loader.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.JsonObject;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import net.minecraft.client.resource.metadata.serializer.ResourceMetadataSerializer;
import net.minecraft.resource.Identifier;
import net.minecraft.resource.pack.CustomPack;
import net.minecraft.resource.pack.Pack;
import net.minecraft.resource.pack.PackType;

import net.ornithemc.osl.resource.loader.api.ModPack;
import net.ornithemc.osl.resource.loader.api.ResourceUtils;

public class BuiltInModPack implements ModPack {

	private final ModContainer mod;
	private final PackType type;
	private final Map<PackType, Set<String>> namespaces;

	private final List<Path> roots;
	//private final String separator;

	private JsonObject generatedPackMetadata;

	public BuiltInModPack(ModContainer mod, PackType type) {
		this.mod = mod;
		this.type = type;
		this.namespaces = new HashMap<>();

		this.roots = this.mod.getRootPaths();

		findNamespaces();
	}

	private void findNamespaces() {
		for (Path root : roots) {
			String separator = root.getFileSystem().getSeparator();

			for (PackType type : PackType.values()) {
				Path dir = root.resolve(type.getDirectory());
				Set<String> ns = new HashSet<>();

				if (Files.isDirectory(dir)) {
					try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
						for (Path p : ds) {
							String s = p.getFileName().toString();
							String namespace = s.replace(separator, "");

							if (ResourceUtils.isValidNamespace(namespace)) {
								ns.add(namespace);
							}
						}
					} catch (IOException e) {
						ResourceLoader.LOGGER.warn("failed to parse namespaces for built-in resource pack for mod " + mod.getMetadata().getId(), e);
					}
				}

				namespaces.put(type, ns);
			}
		}
	}

	@Override
	public InputStream getRootResource(String path) throws IOException {
		if (path.contains("/") || path.contains("\\")) {
			throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
		}

		Path p = getPath(path);

		if (p == null || !Files.exists(p)) {
			if ("pack.mcmeta".equals(path)) {
				JsonObject metadata = generatePackMetadata();
				String serializedMetadata = metadata.toString();

				return new ByteArrayInputStream(serializedMetadata.getBytes(StandardCharsets.UTF_8));
			}
			if ("pack.png".equals(path)) {
				return Pack.class.getResourceAsStream("/" + path);
			}

			return null;
		}

		return Files.newInputStream(p);
	}

	@Override
	public InputStream getResource(PackType type, Identifier location) throws IOException {
		Path path = getPath(type, location);

		if (path == null || !Files.exists(path)) {
			if ("pack.mcmeta".equals(location.getPath())) {
				JsonObject metadata = generatePackMetadata();
				String serializedMetadata = metadata.toString();

				return new ByteArrayInputStream(serializedMetadata.getBytes(StandardCharsets.UTF_8));
			}
			if ("pack.png".equals(location.getPath())) {
				return Pack.class.getResourceAsStream("/" + location.getPath());
			}

			return null;
		}

		return Files.newInputStream(path);
	}

	@Override
	public boolean hasResource(PackType type, Identifier location) {
		Path path = getPath(type, location);
		return path != null && Files.exists(path);
	}

	@Override
	public Collection<Identifier> findResources(PackType type, String path, int maxDepth, Predicate<String> filter) {
		Collection<Identifier> locations = new LinkedHashSet<>();

		for (String namespace : namespaces.get(type)) {
			try {
				locations.addAll(getResources(type, maxDepth, namespace, path, filter));
			} catch (IOException e) {
			}
		}

		return locations;
	}

	private Collection<Identifier> getResources(PackType type, int maxDepth, String namespace, String path, Predicate<String> filter) throws IOException {
		List<Identifier> locations = new ArrayList<>();

		for (Path root : roots) {
			Path dir = root.resolve(type.getDirectory()).resolve(namespace);
			Path start = dir.resolve(path);
			Iterator<Path> it = Files.walk(start).iterator();

			while (it.hasNext()) {
				Path p = it.next();

				if (!p.endsWith(".mcmeta") && Files.isRegularFile(p) && filter.test(p.getFileName().toString())) {
					locations.add(new Identifier(namespace, dir.relativize(p).toString().replaceAll("\\\\", "/")));
				}
			}
		}

		return locations;
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public Set<String> getNamespaces(PackType type) {
		return namespaces.get(type);
	}

	@Override
	public <T> T getMetadataSection(ResourceMetadataSerializer<T> serializer) throws IOException {
		try (InputStream is = getRootResource("pack.mcmeta")) {
			return CustomPack.getMetadataSection(serializer, is);
		}
	}

	@Override
	public String getName() {
		return getModMetadata().getName();
	}

	@Override
	public ModMetadata getModMetadata() {
		return mod.getMetadata();
	}

	private Path getPath(PackType type, Identifier location) {
		return getPath(getPathName(type, location));
	}

	private Path getPath(String path) {
		for (Path root : roots) {
			String separator = root.getFileSystem().getSeparator();
			String pathName = path.replace("/", separator);
			Path p = root.resolve(pathName);

			if (Files.exists(p)) {
				return p;
			}
		}

		return null;
	}

	private String getPathName(PackType type, Identifier location) {
		return String.format("%s/%s/%s", type.getDirectory(), location.getNamespace(), location.getPath());
	}

	private JsonObject generatePackMetadata() {
		if (generatedPackMetadata == null) {
			generatedPackMetadata = new JsonObject();
			JsonObject pack = new JsonObject();
			pack.addProperty("pack_format", ResourceLoader.getPackFormat(type));
			pack.addProperty("description", getModMetadata().getDescription());
			generatedPackMetadata.add("pack", pack);
		}

		return generatedPackMetadata;
	}
}
