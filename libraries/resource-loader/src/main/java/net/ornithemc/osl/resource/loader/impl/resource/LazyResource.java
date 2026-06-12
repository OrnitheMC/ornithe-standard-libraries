package net.ornithemc.osl.resource.loader.impl.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;

public class LazyResource implements Resource {

	private final String sourceName;
	private final NamespacedIdentifier location;
	private final IOSupplier<InputStream> inputStream;
	private final IOSupplier<ResourceMetadata> resourceMetadata;

	private ResourceMetadata metadata;

	public LazyResource(String sourceName, NamespacedIdentifier location, IOSupplier<InputStream> inputStream, IOSupplier<ResourceMetadata> resourceMetadata) {
		this.sourceName = sourceName;
		this.location = location;
		this.inputStream = inputStream;
		this.resourceMetadata = resourceMetadata;
	}

	@Override
	public String sourceName() {
		return this.sourceName;
	}

	@Override
	public NamespacedIdentifier location() {
		return this.location;
	}

	@Override
	public InputStream open() throws IOException {
		return this.inputStream.get();
	}

	@Override
	public BufferedReader openAsReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.inputStream.get(), StandardCharsets.UTF_8));
	}

	@Override
	public ResourceMetadata metadata() throws IOException {
		if (this.metadata == null) {
			this.metadata = this.resourceMetadata.get();
		}

		return this.metadata;
	}

	@Override
	public boolean hasMetadata() {
		return this.resourceMetadata != ResourceMetadata.EMPTY_SUPPLIER;
	}

	@Override
	public void close() throws IOException {
	}
}
