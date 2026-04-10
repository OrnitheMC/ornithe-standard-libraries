package net.ornithemc.osl.text.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.ornithemc.osl.text.api.TextComponent.Serializer;
import net.ornithemc.osl.text.api.TextComponent.Serializer.LowercaseEnumTypeAdapterFactory;
import net.ornithemc.osl.text.impl.LiteralTextComponent;
import net.ornithemc.osl.text.impl.TextResolvers;
import net.ornithemc.osl.text.impl.TranslatableTextComponent;

public final class TextComponents {

	private static final Gson GSON = new GsonBuilder()
		.disableHtmlEscaping()
		.registerTypeHierarchyAdapter(TextComponent.class, new Serializer())
		.registerTypeHierarchyAdapter(Style.class, new Style.Serializer())
		.registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
		.create();

	public static TextComponent literal(String t) {
		return new LiteralTextComponent(t);
	}

	public static TextComponent literal(String t, Object... args) {
		return new LiteralTextComponent(String.format(t, args));
	}

	public static TextComponent translatable(String t) {
		return new TranslatableTextComponent(t);
	}

	public static TextComponent translatable(String t, Object... args) {
		return new TranslatableTextComponent(t, args);
	}

	public static TextComponent resolve(Object o) {
		return TextResolvers.resolve(o);
	}

	public static TextComponent composite(Object... ts) {
		TextComponent c = literal("");

		for (Object t : ts) {
			c.append(resolve(t));
		}

		return c;
	}

	public static TextComponent fromJson(String json) {
		return GSON.fromJson(json, TextComponent.class);
	}

	public static TextComponent fromJsonTree(JsonElement json) {
		return GSON.fromJson(json, TextComponent.class);
	}

	public static String toJson(TextComponent t) {
		return GSON.toJson(t);
	}

	public static JsonElement toJsonTree(TextComponent t) {
		return GSON.toJsonTree(t);
	}
}
