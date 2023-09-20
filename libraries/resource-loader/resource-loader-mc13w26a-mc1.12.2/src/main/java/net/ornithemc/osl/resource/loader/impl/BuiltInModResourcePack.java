package net.ornithemc.osl.resource.loader.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import net.minecraft.client.resource.metadata.ResourceMetadataSection;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.resource.loader.api.ModResourcePack;
import net.ornithemc.osl.resource.loader.api.ResourceUtils;
import net.ornithemc.osl.resource.loader.impl.mixin.client.CustomResourcePackInvoker;

public class BuiltInModResourcePack implements ModResourcePack {

	private final ModContainer mod;
	private final Set<String> namespaces;

	private final List<Path> roots;
	//private final String separator;

	private JsonObject generatedPackMetadata;

	public BuiltInModResourcePack(ModContainer mod) {
		this.mod = mod;
		this.namespaces = new HashSet<>();

		this.roots = this.mod.getRootPaths();

		findNamespaces();
	}

	private void findNamespaces() {
		for (Path root : roots) {
			Path assets = root.resolve("assets");

			if (Files.isDirectory(assets)) {
				String separator = root.getFileSystem().getSeparator();

				try (DirectoryStream<Path> ds = Files.newDirectoryStream(assets)) {
					for (Path p : ds) {
						String s = p.getFileName().toString();
						String namespace = s.replace(separator, "");

						if (ResourceUtils.isValidNamespace(namespace)) {
							namespaces.add(namespace);
						}
					}
				} catch (IOException e) {
					System.out.println("failed to parse namespaces for built-in resource pack for mod " + mod.getMetadata().getId());
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public InputStream getResource(Identifier location) throws IOException {
		Path path = getPath(location);

		if (path == null || !Files.exists(path)) {
			if ("pack.mcmeta".equals(location.getPath())) {
				JsonObject metadata = generatePackMetadata();
				String serializedMetadata = metadata.toString();

				return new ByteArrayInputStream(serializedMetadata.getBytes(StandardCharsets.UTF_8));
			}

			return null;
		}

		return Files.newInputStream(path);
	}

	@Override
	public boolean hasResource(Identifier location) {
		Path path = getPath(location);
		return path != null && Files.exists(path);
	}

	@Override
	public Set<String> getNamespaces() {
		return namespaces;
	}

	@Override
	public <T extends ResourceMetadataSection> T getMetadataSection(ResourceMetadataSerializerRegistry metadataSerializers, String name) throws IOException {
		try (InputStream is = getResource(new Identifier("pack.mcmeta"))) {
			return CustomResourcePackInvoker.invokeGetMetadataSection(metadataSerializers, is, name);
		}
	}

	@Override
	public BufferedImage getIcon() throws IOException {
		return ImageIO.read(getResource(new Identifier("pack.png")));
	}

	@Override
	public String getName() {
		return getModMetadata().getName();
	}

	@Override
	public ModMetadata getModMetadata() {
		return mod.getMetadata();
	}

	private Path getPath(Identifier location) {
		for (Path root : roots) {
			String separator = root.getFileSystem().getSeparator();
			String pathName = getPathName(location).replace("/", separator);
			Path path = root.resolve(pathName);

			if (Files.exists(path)) {
				return path;
			}
		}

		return null;
	}

	private String getPathName(Identifier location) {
		return String.format("/assets/%s/%s", location.getNamespace(), location.getPath());
	}

	private JsonObject generatePackMetadata() {
		if (generatedPackMetadata == null) {
			generatedPackMetadata = new JsonObject();
			JsonObject pack = new JsonObject();
			pack.addProperty("pack_format", ResourceLoader.getPackFormat());
			pack.addProperty("description", getModMetadata().getDescription());
			generatedPackMetadata.add("pack", pack);
		}

		return generatedPackMetadata;
	}
}
