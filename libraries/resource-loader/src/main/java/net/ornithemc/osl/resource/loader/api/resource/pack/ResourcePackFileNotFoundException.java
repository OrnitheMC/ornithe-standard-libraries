package net.ornithemc.osl.resource.loader.api.resource.pack;

import java.io.FileNotFoundException;

public class ResourcePackFileNotFoundException extends FileNotFoundException {

	private static final long serialVersionUID = 1L;

	public ResourcePackFileNotFoundException(ResourcePack pack, String path) {
		super(String.format("'%s' in ResourcePack '%s'", path, pack.getName()));
	}
}
