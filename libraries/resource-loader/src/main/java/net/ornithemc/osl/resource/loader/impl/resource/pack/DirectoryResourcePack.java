package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.nio.file.Path;
import java.util.List;

public class DirectoryResourcePack extends PathResourcePack {

	private final Path directory;

	public DirectoryResourcePack(Path directory) {
		this.directory = directory;
	}

	@Override
	public String getId() {
		return "directory/" + this.directory.getFileName();
	}

	@Override
	public String getName() {
		return this.directory.getFileName().toString();
	}

	@Override
	protected List<Path> findRootPaths() {
		return ResourcePacks.getRootPaths(this.directory, ".");
	}
}
