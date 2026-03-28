package net.ornithemc.osl.resource.loader.impl.resource.pack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePackMetadata;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class SimpleResourcePackMetadata implements ResourcePackMetadata {

	public static final Serializer SERIALIZER = new Serializer();

	private final int format;
	private final TextComponent description;

	public SimpleResourcePackMetadata(int format, TextComponent description) {
		this.format = format;
		this.description = description;
	}

	@Override
	public int format() {
		return this.format;
	}

	@Override
	public TextComponent description() {
		return this.description;
	}

	public static class Serializer implements ResourceMetadata.Section.Serializer<ResourcePackMetadata> {

		private static final String PACK_FORMAT = "pack_format";
		private static final String DESCRIPTION = "description";

		@Override
		public ResourcePackMetadata deserialize(JsonObject json) {
			JsonElement packFormat;
			JsonElement description;

			if (!json.has(PACK_FORMAT) || !(packFormat = json.get(PACK_FORMAT)).isJsonPrimitive()) {
				throw new JsonSyntaxException(PACK_FORMAT + " missing or not an integer!");
			}
			if (!json.has(DESCRIPTION) || (description = json.get(DESCRIPTION)).isJsonNull()) {
				throw new JsonSyntaxException(DESCRIPTION + " missing or null!");
			}

			return new SimpleResourcePackMetadata(
				packFormat.getAsInt(),
				TextComponents.fromJsonTree(description)
			);
		}

		public JsonObject serialize(ResourcePackMetadata metadata) {
			JsonObject json = new JsonObject();

			json.addProperty(PACK_FORMAT, metadata.format());
			json.add(DESCRIPTION, TextComponents.toJsonTree(metadata.description()));

			return json;
		}
	}
}
