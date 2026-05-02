package net.ornithemc.osl.text.impl;

import java.lang.reflect.Method;

public interface Locale {

	static Locale find() {
		try {
			Class<?> type = Class.forName("net.ornithemc.osl.localization.impl.Locale");
			Method instance = type.getMethod("instance");

			return (Locale) instance.invoke(null);
		} catch (Throwable t) {
			return new Locale() {

				@Override
				public long getLastUpdateTime() {
					return 0;
				}

				@Override
				public String get(String key) {
					return key;
				}
			};
		}
	}

	long getLastUpdateTime();

	String get(String key);

}
