package net.ornithemc.osl.text.impl;

import java.util.HashMap;
import java.util.Map;

import net.ornithemc.osl.text.api.TextComponent;

public class TextResolvers {

	private static Map<Class<?>, TextResolver<?>> resolvers = new HashMap<>();

	static {
		register(String.class, new FormattedStringResolver());
	}

	public static <T> void register(Class<T> type, TextResolver<T> resolver) {
		if (resolvers.containsKey(type)) {
			throw new IllegalStateException("cannot register multiple text component resolvers for type " + type.getName());
		} else {
			resolvers.put(type, resolver);
		}
	}

	public static TextComponent resolve(Object o) {
		Class<?> type = o.getClass();
		TextResolver<?> resolver = resolvers.get(type);

		if (resolver == null) {
			return resolve(o.toString());
		}

		return resolve(resolver, o);
	}

	@SuppressWarnings("unchecked")
	private static <T> TextComponent resolve(TextResolver<T> resolver, Object o) {
		return resolver.resolve((T) o);
	}
}
