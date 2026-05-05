package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.resource.pack.BuiltInTexturePack;
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
		if (pack.getDescriptionLine1() != null) {
			description += pack.getDescriptionLine1();
			if (pack.getDescriptionLine2() != null) {
				description += "\n" + pack.getDescriptionLine2();
			}
		}

		this.pack = pack;
		this.metadata = ResourcePacks.generateMetadataFile(
			TextComponents.literal(description)
		);
	}

	public static String getId(TexturePack pack) {
		return pack instanceof BuiltInTexturePack ? pack.getName() : "texturepack/" + pack.getKey();
	}

	@Override
	public String getId() {
		return getId(this.pack);
	}

	@Override
	public String getName() {
		return this.pack.getName();
	}

	@Override
	public boolean hasResource(String path) {
		return this.pack.hasResource(path, false);
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		if (METADATA_FILE.equals(path)) {
			return this.metadata.get();
		}
		// TODO: pack.png

		return this.pack.getResource(path, false);
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
	}

	@Override
	public void close() {
	}
}
