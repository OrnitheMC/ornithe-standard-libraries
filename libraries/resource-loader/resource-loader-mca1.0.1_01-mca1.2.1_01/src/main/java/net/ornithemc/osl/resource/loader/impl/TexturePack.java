package net.ornithemc.osl.resource.loader.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.AbstractResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourceConsumer;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;
import net.ornithemc.osl.text.api.TextComponents;

public class TexturePack extends AbstractResourcePack {

	public static final String METADATA_FILE = "pack.txt";

	private final ResourcePack delegate;
	private final IOSupplier<InputStream> metadata;

	public String descriptionLine1 = "";
	public String descriptionLine2 = "";

	public TexturePack(ResourcePack delegate) {
		this.delegate = delegate;
		this.metadata = this.createMetadata();
	}

	private IOSupplier<InputStream> createMetadata() {
		this.initMetadata();

		String description = "";
		if (this.descriptionLine1 != null) {
			description += this.descriptionLine1;
			if (!this.descriptionLine2.isEmpty()) {
				description += "\n" + this.descriptionLine2;
			}
		}

		return ResourcePacks.generateMetadataFile(
			TextComponents.literal(description)
		);
	}

	void initMetadata() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getResource(METADATA_FILE)))) {
			this.descriptionLine1 = this.trimDescriptionLine(br.readLine());
			this.descriptionLine2 = this.trimDescriptionLine(br.readLine());
		} catch (IOException e) {
			ResourceLoader.LOGGER.warn("Unable to read metadata from texture pack " + this.getName(), e);
		}
	}

	String trimDescriptionLine(String line) {
		if (line.length() > 34) {
			line = line.substring(0, 34);
		}

		return line;
	}

	@Override
	public String getName() {
		return this.delegate.getName();
	}

	@Override
	public boolean hasResource(String path) {
		if (ResourcePack.METADATA_FILE.equals(path)) {
			return true;
		}

		return this.delegate.hasResource(path);
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		if (ResourcePack.METADATA_FILE.equals(path)) {
			return this.metadata.get();
		}

		return this.delegate.getResource(path);
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
		this.delegate.open();
	}

	@Override
	public void close() {
		this.delegate.close();
	}
}
