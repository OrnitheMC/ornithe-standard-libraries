package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.core.impl.util.ModLoader;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.text.api.TextComponents;

public class BuiltInModResourcesPack extends CompositeResourcePack {

	private final IOSupplier<InputStream> metadata;
	private final IOSupplier<InputStream> icon;

	public BuiltInModResourcesPack(List<ResourcePack> resourcePacks) {
		super(resourcePacks);

		this.metadata = ResourcePacks.generateMetadataFile(
			TextComponents.literal("Built-in " + ModLoader.resolve().displayName() + " mod resources, provided by OSL.")
		);
		this.icon = ModContainerResourcePack.getIcon(ModLoader.resolve().modId());
	}

	@Override
	public String getName() {
		return ModLoader.resolve().displayName() + " Mod Resources";
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		if (METADATA_FILE.equals(path)) {
			return this.metadata.get();
		}
		if (ICON_FILE.equals(path)) {
			return this.icon.get();
		}

		return super.getResource(path);
	}
}
