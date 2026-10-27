package net.ornithemc.osl.resource.loader.impl.adapter;

import net.minecraft.resource.Identifier;

/**
 * Path-only Identifier impl for accessing direct-path resources from the jar and the assets index.
 */
public class FilePathIdentifier extends Identifier {

	public FilePathIdentifier(String path) {
		super(path);
	}

	@Override
	public String toString() {
		return this.getPath();
	}
}
