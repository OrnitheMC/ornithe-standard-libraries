package net.ornithemc.osl.resource.loader.impl.world;

import java.io.File;

public interface WorldStorageExtensionImpl {

	default File getDirectory() {
		throw new AbstractMethodError();
	}
}
