package net.ornithemc.osl.text.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import net.ornithemc.osl.text.impl.LiteralTextComponent;
import net.ornithemc.osl.text.impl.TranslatableTextComponent;

public interface TextComponent {

	Style getStyle();

	List<TextComponent> getSiblings();

	TextComponent format(Formatting... formattings);

	TextComponent format(Style style);

	TextComponent format(UnaryOperator<Style> styler);

	TextComponent append(String text);

	TextComponent append(TextComponent text);

	String buildString();

	String buildFormattedString();

	<T> Optional<T> visit(TextVisitor<T> visitor);

	<T> Optional<T> visit(Style fallback, StyledTextVisitor<T> visitor);

	TextComponent copy();

	TextComponent deepCopy();

	interface Builder {

		Builder append(String text);

		Builder append(TextComponent text);

		TextComponent build();

	}

	class Serializer implements JsonDeserializer<TextComponent>, JsonSerializer<TextComponent> {

		private static final String TEXT = "text";
		private static final String TRANSLATE = "translate";
		private static final String WITH = "with";
		private static final String EXTRA = "extra";

		@Override
		public TextComponent deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			if (json.isJsonPrimitive()) {
				return TextComponents.literal(json.getAsString());
			} else if (json.isJsonArray()) {
				JsonArray siblingsJson = json.getAsJsonArray();
				TextComponent text = null;

				for (JsonElement siblingJson : siblingsJson) {
					TextComponent sibling = this.deserialize(siblingJson, siblingJson.getClass(), context);

					if (text == null) {
						text = sibling;
					} else {
						text.append(sibling);
					}
				}

				return text;
			} else if (json.isJsonObject()) {
				JsonObject textJson = json.getAsJsonObject();

				TextComponent text;

				if (textJson.has(TEXT)) {
					text = TextComponents.literal(textJson.get(TEXT).getAsString());
				} else if (textJson.has(TRANSLATE)) {
					String key = textJson.get(TRANSLATE).getAsString();

					if (textJson.has(WITH)) {
						JsonArray argsJson = textJson.get(WITH).getAsJsonArray();
						Object[] args = new Object[argsJson.size()];

						for (int i = 0; i < args.length; i++) {
							args[i] = context.deserialize(argsJson.get(i), TextComponent.class);

							if (args[i] instanceof LiteralTextComponent) {
								LiteralTextComponent arg = (LiteralTextComponent) args[i];

								if (arg.getStyle().isEmpty() && arg.getSiblings().isEmpty()) {
									args[i] = arg.getValue();
								}
							}
						}

						text = TextComponents.translatable(key, args);
					} else {
						text = TextComponents.translatable(key);
					}
				} else {
					throw new JsonParseException("Don't know how to turn " + json + " into a TextComponent");
				}

				if (textJson.has(EXTRA)) {
					JsonArray siblingsJson = textJson.get(EXTRA).getAsJsonArray();

					if (siblingsJson.size() <= 0) {
						throw new JsonParseException("Unexpected empty array of TextComponents");
					}

					for (int i = 0; i < siblingsJson.size(); i++) {
						text.append(this.deserialize(siblingsJson.get(i), type, context));
					}
				}

				text.format((Style) context.deserialize(json, Style.class));

				return text;
			} else {
				throw new JsonParseException("Don't know how to turn " + json + " into a TextComponent");
			}
		}

		private void serializeStyle(Style style, JsonObject textJson, JsonSerializationContext context) {
			JsonElement styleJson = context.serialize(style);

			if (styleJson.isJsonObject()) {
				for (Map.Entry<String, JsonElement> entry : styleJson.getAsJsonObject().entrySet()) {
					textJson.add(entry.getKey(), entry.getValue());
				}
			}
		}

		@Override
		public JsonElement serialize(TextComponent text, Type type, JsonSerializationContext context) {
			JsonObject textJson = new JsonObject();

			if (!text.getStyle().isEmpty()) {
				this.serializeStyle(text.getStyle(), textJson, context);
			}

			if (!text.getSiblings().isEmpty()) {
				JsonArray jsonArray = new JsonArray();

				for (TextComponent sibling : text.getSiblings()) {
					jsonArray.add(this.serialize(sibling, sibling.getClass(), context));
				}

				textJson.add("extra", jsonArray);
			}

			if (text instanceof LiteralTextComponent) {
				textJson.addProperty(TEXT, ((LiteralTextComponent) text).getValue());
			} else if (text instanceof TranslatableTextComponent) {
				TranslatableTextComponent translatableText = (TranslatableTextComponent) text;

				textJson.addProperty(TRANSLATE, translatableText.getKey());

				if (translatableText.getArgs() != null && translatableText.getArgs().length > 0) {
					JsonArray argsJson = new JsonArray();

					for (Object arg : translatableText.getArgs()) {
						if (arg instanceof TextComponent) {
							argsJson.add(this.serialize((TextComponent) arg, arg.getClass(), context));
						} else {
							argsJson.add(new JsonPrimitive(String.valueOf(arg)));
						}
					}

					textJson.add(WITH, argsJson);
				}
			} else {
				throw new IllegalArgumentException("Don't know how to serialize " + text + " as a TextComponent");
			}

			return textJson;
		}

		public static class LowercaseEnumTypeAdapterFactory implements TypeAdapterFactory {

			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> token) {
				@SuppressWarnings("unchecked")
				Class<T> type = (Class<T>) token.getRawType();

				if (!type.isEnum()) {
					return null;
				}

				Map<String, T> constants = new HashMap<>();

				for (T constant : type.getEnumConstants()) {
					constants.put(this.toString(constant), constant);
				}

				return new TypeAdapter<T>() {

					@Override
					public void write(JsonWriter writer, T constant) throws IOException {
						if (constant == null) {
							writer.nullValue();
						} else {
							writer.value(LowercaseEnumTypeAdapterFactory.this.toString(constant));
						}
					}

					@Override
					public T read(JsonReader reader) throws IOException {
						if (reader.peek() == JsonToken.NULL) {
							reader.nextNull();
							return null;
						} else {
							return constants.get(reader.nextString());
						}
					}
				};
			}

			private String toString(Object constant) {
				return constant instanceof Enum
					? ((Enum<?>) constant).name().toLowerCase(Locale.ROOT)
					: constant.toString().toLowerCase(Locale.ROOT);
			}
		}
	}
}
