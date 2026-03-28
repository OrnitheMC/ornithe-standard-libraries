package net.ornithemc.osl.resource.loader.impl.resource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata.Section.Serializer;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePackMetadata;
import net.ornithemc.osl.resource.loader.impl.resource.pack.SimpleResourcePackMetadata;

public class JsonResourceMetadata implements ResourceMetadata {

	private static final Gson GSON = new GsonBuilder().create();

	public static JsonResourceMetadata fromInputStream(IOSupplier<InputStream> inputStream) throws IOException {
		return fromInputStream(inputStream.get());
	}

	public static JsonResourceMetadata fromInputStream(InputStream is) throws IOException {
		JsonResourceMetadata resourceMetadata;

		try {
			Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			JsonObject metadata = GSON.fromJson(reader, JsonObject.class);

			resourceMetadata = new JsonResourceMetadata(metadata);
		} catch (Throwable t) {
			if (is != null) {
				try {
					is.close();
				} catch (Throwable ct) {
					t.addSuppressed(ct);
				}
			}

			throw t;
		}

		if (is != null) {
			is.close();
		}

		return resourceMetadata;
	}

	public static IOSupplier<InputStream> asInputStream(ResourcePackMetadata packSection) {
		JsonObject json = new JsonObject();

		json.add(
			ResourcePackMetadata.NAME,
			SimpleResourcePackMetadata.SERIALIZER.serialize(packSection)
		);

		return () -> new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));
	}

	private final JsonObject metadata;

	public JsonResourceMetadata(JsonObject metadata) {
		this.metadata = metadata;
	}

	public JsonObject asJsonObject() {
		return this.metadata;
	}

	@Override
	public <T> T getSection(String name, Serializer<T> serializer) {
		return !this.metadata.has(name) ? null : serializer.deserialize(this.metadata.getAsJsonObject(name));
	}
}
