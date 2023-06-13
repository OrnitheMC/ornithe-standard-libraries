package net.ornithemc.osl.resource.loader.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.ModMetadata;

import net.minecraft.client.resource.metadata.ResourceMetadataSection;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.resource.loader.api.ModResourcePack;
import net.ornithemc.osl.resource.loader.api.ResourceUtils;
import net.ornithemc.osl.resource.loader.impl.mixin.client.CustomResourcePackInvoker;

public class BuiltInModResourcePack implements ModResourcePack {

	private final ModContainer mod;
	private final Set<String> namespaces;

	private final Path root;
	private final String separator;

	public BuiltInModResourcePack(ModContainer mod) {
		this.mod = mod;
		this.namespaces = new HashSet<>();

		this.root = this.mod.rootPath();
		this.separator = this.root.getFileSystem().getSeparator();

		findNamespaces();
	}

	private void findNamespaces() {
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(root)) {
			for (Path p : ds) {
				String s = p.getFileName().toString();
				String namespace = s.replace(separator, "");

				if (ResourceUtils.isValidNamespace(namespace)) {
					namespaces.add(namespace);
				}
			}
		} catch (IOException e) {
			ResourceLoader.LOGGER.warn("failed to parse namespaces for built-in resource pack for mod " + mod.metadata().id(), e);
		}
	}

	@Override
	public InputStream getResource(Identifier location) throws IOException {
		Path path = getPath(location);

		if (!Files.exists(path)) {
			return null;
		}

		return Files.newInputStream(path);
	}

	@Override
	public boolean hasResource(Identifier location) {
		return Files.exists(getPath(location));
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
		return getModMetadata().name();
	}

	@Override
	public ModMetadata getModMetadata() {
		return mod.metadata();
	}

	private Path getPath(Identifier location) {
		return root.resolve(getPathName(location));
	}

	private String getPathName(Identifier location) {
		return String.format("/assets/%s/%s", location.getNamespace(), location.getPath()).replace("/", separator);
	}
}
