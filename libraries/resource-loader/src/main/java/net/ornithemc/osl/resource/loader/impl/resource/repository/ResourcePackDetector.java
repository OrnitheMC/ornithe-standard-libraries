package net.ornithemc.osl.resource.loader.impl.resource.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.pack.DirectoryResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ZippedResourcePack;

public class ResourcePackDetector {

	public ResourcePack detectResourcePack(Path file) throws IOException {
		BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);

		if (attributes.isDirectory()) {
			if (this.isValidDirectory(file)) {
				return this.createDirectoryPack(file);
			}
		} else if (attributes.isRegularFile()) {
			if (this.isValidZip(file)) {
				return this.createZippedPack(file);
			}
		}

		return null;
	}

	protected boolean isValidDirectory(Path file) {
		return Files.exists(file.resolve(ResourcePack.METADATA_FILE));
	}

	protected boolean isValidZip(Path file) {
		return file.getFileName().toString().endsWith(".zip");
	}

	protected ResourcePack createDirectoryPack(Path file) {
		return new DirectoryResourcePack(file);
	}

	protected ResourcePack createZippedPack(Path file) {
		return new ZippedResourcePack(file);
	}
}
