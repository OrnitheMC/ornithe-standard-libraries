package net.ornithemc.osl.text.impl;

import java.util.HashMap;
import java.util.Map;

import net.ornithemc.osl.text.api.TextComponent;

public class TextConverters {

	private static Map<Class<?>, TextConverter<?>> converters = new HashMap<>();

	static {
		register(String.class, TextComponent::buildFormattedString);
	}

	public static <T> void register(Class<T> type, TextConverter<T> resolver) {
		if (converters.containsKey(type)) {
			throw new IllegalStateException("cannot register multiple text component converters for type " + type.getName());
		} else {
			converters.put(type, resolver);
		}
	}

	public static <T> T convert(TextComponent text, Class<T> type) {
		TextConverter<?> converter = converters.get(type);

		if (converter == null) {
			return null;
		}

		return convert(converter, text);
	}

	@SuppressWarnings("unchecked")
	private static <T> T convert(TextConverter<?> converter, TextComponent text) {
		return (T) converter.convert(text);
	}
}
