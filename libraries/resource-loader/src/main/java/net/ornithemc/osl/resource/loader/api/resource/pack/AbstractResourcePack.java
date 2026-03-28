package net.ornithemc.osl.resource.loader.api.resource.pack;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata.Section.Serializer;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.resource.JsonResourceMetadata;

/**
 * A partial resource pack implementation that handles namespaces and resource pack metadata sections.
 */
public abstract class AbstractResourcePack implements ResourcePack {

	private Map<ResourceType, Set<String>> namespaces;

	@Override
	public Set<String> getNamespaces(ResourceType type) {
		if (this.namespaces == null) {
			this.namespaces = this.findNamespaces();
		}

		return this.namespaces.getOrDefault(type, Collections.emptySet());
	}

	protected abstract Map<ResourceType, Set<String>> findNamespaces();

	@Override
	public <T> T getMetadata(String name, Serializer<T> serializer) throws IOException {
		InputStream resource = this.getResource(METADATA_FILE);

		if (resource != null) {
			try {
				ResourceMetadata metadata = JsonResourceMetadata.fromInputStream(resource);

				if (metadata != null) {
					return metadata.getSection(name, serializer);
				}
			} catch (Exception e) {
				ResourceLoader.LOGGER.error("Could not load {} {} metadata: {}", this.getId(), name, e);
			}
		}

		return null;
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
		this.namespaces = null;
	}

	@Override
	public void finalize() {
		this.close();
	}
}
