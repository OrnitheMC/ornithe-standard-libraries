package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class ZippedResourcePack extends FileResourcePack {

	private FileSystem zipFs;
	private boolean zipFailed;

	public ZippedResourcePack(Path file) {
		super(file);
	}

	@Override
	protected List<Path> findRootPaths() {
		if (this.zipFs == null && !this.zipFailed) {
			try {
				this.zipFs = FileSystems.newFileSystem(this.file, (ClassLoader) null);
				this.zipFailed = false;
			} catch (IOException e) {
				this.zipFs = null;
				this.zipFailed = true;
			}
		}

		if (this.zipFs == null) {
			return Collections.emptyList();
		} else {
			return ResourcePacks.getRootPaths(this.zipFs.getRootDirectories(), ".");
		}
	}

	@Override
	public void close() {
		super.close();

		if (this.zipFs != null) {
			try {
				this.zipFs.close();
			} catch (IOException e) {
			}
		}

		this.zipFs = null;
		this.zipFailed = false;
	}
}
