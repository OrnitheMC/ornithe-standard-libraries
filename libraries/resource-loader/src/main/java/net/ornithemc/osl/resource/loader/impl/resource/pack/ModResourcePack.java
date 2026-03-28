package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.text.api.TextComponents;

public class ModResourcePack extends ModContainerResourcePack {

	private final IOSupplier<InputStream> metadata;
	private final IOSupplier<InputStream> icon;

	public ModResourcePack(ModContainer mod) {
		super(mod);

		this.metadata = ResourcePacks.generateMetadataFile(
			TextComponents.literal(mod.getMetadata().getDescription())
		);
		this.icon = ModContainerResourcePack.getIcon(mod);
	}

	public ModResourcePack(ModContainer mod, String directory, String id, String name) {
		super(mod, directory, id, name);

		this.metadata = ResourcePacks.generateMetadataFile(
			TextComponents.literal(mod.getMetadata().getDescription())
		);
		this.icon = ModContainerResourcePack.getIcon(mod);
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		try {
			return super.getResource(path);
		} catch (FileNotFoundException e) {
			if (METADATA_FILE.equals(path)) {
				return this.metadata.get();
			}
			if (ICON_FILE.equals(path)) {
				return this.icon.get();
			}

			throw e;
		}
	}
}
