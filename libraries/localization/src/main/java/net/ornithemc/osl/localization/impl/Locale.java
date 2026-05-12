package net.ornithemc.osl.localization.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.quiltmc.parsers.json.JsonReader;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

public final class Locale implements net.ornithemc.osl.text.impl.Locale {

	private static final Locale INSTANCE = new Locale();

	public static Locale instance() {
		return INSTANCE;
	}

	// java.util.Map <=1.12.2, java.util.Properties >1.12.2
	private Map<String, String> map;
	private Properties properties;

	private long lastUpdateTime;

	private Locale() {
		this.map = new HashMap<>();
	}

	public void wrap(Map<String, String> map) {
		this.init(map, null);
	}

	public void wrap(Properties properties) {
		this.init(null, properties);
	}

	private void init(Map<String, String> map, Properties properties) {
		this.map = map;
		this.properties = properties;
	}

	public String get(String key) {
		if (this.map != null) {
			return this.map.get(key);
		} else if (this.properties != null) {
			return this.properties.getProperty(key);
		} else {
			return null;
		}
	}

	public String getOrDefault(String key, String defaultValue) {
		if (this.map != null) {
			return this.map.getOrDefault(key, defaultValue);
		} else if (this.properties != null) {
			return this.properties.getProperty(key, defaultValue);
		} else {
			return defaultValue;
		}
	}

	public boolean containsKey(String key) {
		if (this.map != null) {
			return this.map.containsKey(key);
		} else if (this.properties != null) {
			return this.properties.containsKey(key);
		} else {
			return false;
		}
	}

	private void set(String key, String translation) {
		if (this.map != null) {
			this.map.put(key, translation);
		} else if (this.properties != null) {
			this.properties.setProperty(key, translation);
		}
	}

	public long getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime() {
		this.lastUpdateTime = System.currentTimeMillis();
	}

	public void reload(ResourceManager resourceManager, List<String> languages) {
		for (String language : languages) {
			this.loadLanguage(resourceManager, language);
		}

		this.setLastUpdateTime();
	}

	public void loadLanguage(ResourceManager resourceManager, String language) {
		String pathFormat = "lang/%s%s";
		String[] paths;

		if (ResourcePacks.getSupportedFormat() < 3) {
			String languageLowercase = language.toLowerCase(java.util.Locale.ROOT);

			paths = new String[] {
				String.format(pathFormat, language, ".lang"),
				String.format(pathFormat, language, ".json"),
				String.format(pathFormat, languageLowercase, ".lang"),
				String.format(pathFormat, languageLowercase, ".json")
			};
		} else {
			paths = new String[] {
				String.format(pathFormat, language, ".lang"),
				String.format(pathFormat, language, ".json")
			};
		}

		for (String path : paths) {
			try {
				this.loadFromResources(path, resourceManager.getResourceStack(path));
			} catch (IOException ignored) {
			}

			for (String namespace : resourceManager.getNamespaces()) {
				NamespacedIdentifier location = NamespacedIdentifiers.from(namespace, path);
				List<Resource> resources = resourceManager.getResourceStack(location);

				this.loadFromResources(resources);
			}
		}
	}

	public void loadFromResources(String path, List<InputStream> resources) {
		for (InputStream resource : resources) {
			try {
				this.loadFromResource(path, resource);
			} catch (IOException e) {
				Localization.LOGGER.warn("Error parsing language file {}: {}", path, e);
			}
		}
	}

	public void loadFromResources(List<Resource> resources) {
		for (Resource resource : resources) {
			try {
				this.loadFromResource(resource.location(), resource.open());
			} catch (IOException e) {
				Localization.LOGGER.warn("Error parsing language file {} ({}): {}", resource.location(), resource.sourceName(), e);
			}
		}
	}

	public void loadFromResource(String path, InputStream resource) throws IOException {
		if (path.endsWith(".lang")) {
			this.loadFromLang(resource);
		} else if (path.endsWith(".json")) {
			this.loadFromJson(resource);
		} else {
			Localization.LOGGER.warn("Skipping language file of unknown type: {}", path);
		}
	}

	public void loadFromResource(NamespacedIdentifier location, InputStream resource) throws IOException {
		if (location.identifier().endsWith(".lang")) {
			this.loadFromLang(resource);
		} else if (location.identifier().endsWith(".json")) {
			this.loadFromJson(resource);
		} else {
			Localization.LOGGER.warn("Skipping language file of unknown type: {}", location);
		}
	}

	public void loadFromLang(InputStream is) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				String[] args = line.split("=", 2);
				if (args.length != 2) {
					continue;
				}
				this.set(args[0], args[1]);
			}
		}
	}

	public void loadFromJson(InputStream is) throws IOException {
		try (JsonReader reader = JsonReader.json(new InputStreamReader(is))) {
			reader.beginObject();
			while (reader.hasNext()) {
				this.set(reader.nextName() , reader.nextString());
			}
			reader.endObject();
		}
	}
}
