package net.ornithemc.osl.resource.loader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import net.minecraft.client.resource.pack.AbstractTexturePack;

import net.ornithemc.osl.resource.loader.api.ModTexturePack;

public class BuiltInModTexturePack extends AbstractTexturePack implements ModTexturePack {

	private final ModContainer mod;

	private final List<Path> roots;

	public BuiltInModTexturePack(ModContainer mod) {
		super(mod.getMetadata().getId(), mod.getMetadata().getName());

		this.mod = mod;

		this.roots = this.mod.getRootPaths();
	}

	@Override
	public InputStream getResource(String location) {
		Path path = getPath(location);

		if (path == null || !Files.exists(path)) {
			return null;
		}

		// a bit of a hack to re-throw a checked exception
		// because proguard stripped the exceptions off this method
		try {
			return Files.newInputStream(path);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

//	@Override
	public boolean hasResource(String location) {
		Path path = getPath(location);
		return path != null && Files.exists(path);
	}

	@Override
	public boolean checkCompatibility() {
		return true;
	}

	@Override
	public ModMetadata getModMetadata() {
		return mod.getMetadata();
	}

	private Path getPath(String location) {
		for (Path root : roots) {
			String separator = root.getFileSystem().getSeparator();
			String pathName = location.replace("/", separator);
			Path path = root.resolve(pathName);

			if (Files.exists(path)) {
				return path;
			}
		}

		return null;
	}
}
