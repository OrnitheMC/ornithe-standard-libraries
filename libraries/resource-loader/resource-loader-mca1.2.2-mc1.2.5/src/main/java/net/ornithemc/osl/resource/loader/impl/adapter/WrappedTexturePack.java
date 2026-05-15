package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.resource.pack.TexturePack;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.AbstractResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourceConsumer;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;
import net.ornithemc.osl.text.api.TextComponents;

public class WrappedTexturePack extends AbstractResourcePack {

	private final TexturePack pack;
	private final IOSupplier<InputStream> metadata;

	public WrappedTexturePack(TexturePack pack) {
		String description = "";
		if (pack.descriptionLine1 != null) {
			description += pack.descriptionLine1;
			if (pack.descriptionLine2 != null) {
				description += "\n" + pack.descriptionLine2;
			}
		}

		this.pack = pack;
		this.metadata = ResourcePacks.generateMetadataFile(
			TextComponents.literal(description)
		);
	}

	@Override
	public String getName() {
		return this.pack.name;
	}

	@Override
	public boolean hasResource(String path) {
		try {
			return this.pack.getResource(path) != null;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		if (METADATA_FILE.equals(path)) {
			return this.metadata.get();
		}

		return this.pack.getResource(path);
	}

	@Override
	protected Map<ResourceType, Set<String>> findNamespaces() {
		return Collections.emptyMap();
	}

	@Override
	public boolean hasResource(ResourceType type, NamespacedIdentifier location) {
		return false;
	}

	@Override
	public IOSupplier<InputStream> getResource(ResourceType type, NamespacedIdentifier location) {
		return null;
	}

	@Override
	public void findResources(ResourceType type, String namespace, String directory, ResourceConsumer consumer) {
	}

	@Override
	public void open() {
		this.pack.open();
	}

	@Override
	public void close() {
		this.pack.close();
	}
}
