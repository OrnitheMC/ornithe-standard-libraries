package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.nio.file.Path;
import java.util.List;

public class DirectoryResourcePack extends FileResourcePack {

	public DirectoryResourcePack(Path directory) {
		super(directory);
	}

	@Override
	protected List<Path> findRootPaths() {
		return ResourcePacks.getRootPaths(this.file, ".");
	}
}
