package net.ornithemc.osl.resource.loader.impl.resource.pack;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.fabricmc.loader.api.FabricLoader;

import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.text.api.TextComponents;

public class BuiltInModResourcesPack extends CompositeResourcePack {

	private static final ModLoader MOD_LOADER = ModLoader.get();

	private final IOSupplier<InputStream> metadata;
	private final IOSupplier<InputStream> icon;

	public BuiltInModResourcesPack(List<ResourcePack> resourcePacks) {
		super(resourcePacks);

		this.metadata = ResourcePacks.generateMetadataFile(
			TextComponents.literal("Built-in " + MOD_LOADER.displayName() + " Mod Resources")
		);
		this.icon = ModContainerResourcePack.getIcon(MOD_LOADER.modId());
	}

	@Override
	public String getId() {
		return MOD_LOADER.id() + "-mod-resources";
	}

	@Override
	public String getName() {
		return "Mod Resources";
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

	private enum ModLoader {

		FABRIC("fabric", "fabricloader", "Fabric"),
		QUILT ("quilt" , "quilt_loader", "Quilt");

		private final String id;
		private final String modId;
		private final String displayName;

		private ModLoader(String id, String modId, String displayName) {
			this.id = id;
			this.modId = modId;
			this.displayName = displayName;
		}

		String id() {
			return this.id;
		}

		String modId() {
			return this.modId;
		}

		String displayName() {
			return this.displayName;
		}

		static ModLoader get() {
			for (ModLoader loader : values()) {
				if (FabricLoader.getInstance().isModLoaded(loader.modId)) {
					return loader;
				}
			}

			throw new RuntimeException("Neither Fabric Loader nor Quilt Loader are loaded?! How is this even running?!");
		}
	}
}
