package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.io.InputStream;

import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.text.api.TextComponents;

public class ModResourcePack extends ModContainerResourcePack {

	public ModResourcePack(ModContainer mod) {
		this(mod, ".", mod.getMetadata().getName());
	}

	public ModResourcePack(ModContainer mod, String directory, String name) {
		super(mod, directory, name);
	}

	@Override
	protected IOSupplier<InputStream> generateMetadataFile() {
		return ResourcePacks.generateMetadataFile(
			TextComponents.literal(this.mod.getMetadata().getDescription())
		);
	}

	@Override
	protected IOSupplier<InputStream> generateIconFile() {
		return ModContainerResourcePack.getIcon(this.mod);
	}
}
