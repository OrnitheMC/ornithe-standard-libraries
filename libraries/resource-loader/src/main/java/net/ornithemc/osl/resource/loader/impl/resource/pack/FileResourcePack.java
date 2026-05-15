package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.nio.file.Path;

public abstract class FileResourcePack extends PathResourcePack {

	final Path file;

	public FileResourcePack(Path file) {
		this.file = file;
	}

	@Override
	public String getName() {
		return this.file.getFileName().toString();
	}
}
