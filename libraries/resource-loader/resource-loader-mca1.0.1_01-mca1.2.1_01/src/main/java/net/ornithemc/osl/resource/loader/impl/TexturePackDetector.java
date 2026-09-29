package net.ornithemc.osl.resource.loader.impl;

import java.nio.file.Files;
import java.nio.file.Path;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.ResourcePackDetector;

public class TexturePackDetector extends ResourcePackDetector {

	@Override
	protected boolean isValidDirectory(Path file) {
		return Files.exists(file.resolve(TexturePack.METADATA_FILE));
	}

	@Override
	protected ResourcePack createDirectoryPack(Path file) {
		return new TexturePack(super.createDirectoryPack(file));
	}

	@Override
	protected ResourcePack createZippedPack(Path file) {
		return new TexturePack(super.createZippedPack(file));
	}
}
