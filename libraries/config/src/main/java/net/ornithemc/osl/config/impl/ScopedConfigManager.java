package net.ornithemc.osl.config.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.serdes.FileSerializerType;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.config.api.serdes.config.ConfigSerializer;
import net.ornithemc.osl.config.api.serdes.config.ConfigSerializers;
import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;

public final class ScopedConfigManager {

	private static final String CONFIG_DIR_NAME = "config";

	private final ConfigScope scope;
	private final Path rootDir;

	private final Map<String, Config> configs;

	public ScopedConfigManager(ConfigScope scope, Path rootDir) {
		this.scope = scope;
		this.rootDir = rootDir;

		this.configs = new LinkedHashMap<>();
	}

	public ConfigScope getScope() {
		return scope;
	}

	public Path getConfigDir() {
		return rootDir.resolve(CONFIG_DIR_NAME);
	}

	public void register(Config config) {
		configs.compute(key(config.getNamespace(), config.getName()), (key, value) -> {
			if (value == null) {
				config.init();
				value = config;
			} else {
				if (value == config) {
					return value; // odd thing to register a config multiple times, but okay
				} else {
					throw new IllegalStateException("config " + key + " is not unique");
				}
			}

			return value;
		});
	}

	private static String key(String namespace, String name) {
		return namespace == null ? name : (namespace + ":" + name);
	}

	public Collection<Config> getConfigs() {
		return configs.values();
	}

	public void load(LoadingPhase phase) {
		for (Config config : getConfigs()) {
			if (config.getLoadingPhase() == phase) {
				config.load();
				// ensure that values that are not present in
				// the config file are reset to default values
				config.resetAll();
				guardRead(config);
			}
		}
	}

	public void unload() {
		for (Config config : getConfigs()) {
			guardWrite(config);
			config.unload();
		}
	}

	public void save() {
		for (Config config : getConfigs()) {
			guardWrite(config);
		}
	}

	private void guardWrite(Config config) {
		SerializationSettings settings = SerializationSettings.forFile();
		FileSerializerType<?> type = config.getType();

		try {
			write(config, settings, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private <M> void write(Config config, SerializationSettings settings, FileSerializerType<M> type) throws IOException {
		ConfigSerializer<M> serializer = ConfigSerializers.get(type);

		if (serializer == null) {
			throw new IOException("don't know how to serialize " + config + " to file!");
		} else {
			Path path = getFilePath(config);

			if (!Files.exists(path)) {
				Files.createDirectories(path.getParent());
			}

			serializer.serialize(config, settings, type.open(path));
		}
	}

	private void guardRead(Config config) {
		SerializationSettings settings = SerializationSettings.forFile();
		FileSerializerType<?> type = config.getType();

		try {
			read(config, settings, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private <M> void read(Config config, SerializationSettings settings, FileSerializerType<M> type) throws IOException {
		ConfigSerializer<M> serializer = ConfigSerializers.get(type);

		if (serializer == null) {
			throw new IOException("don't know how to deserialize " + config + " from file!");
		} else {
			Path path = getFilePath(config);

			if (Files.exists(path)) {
				serializer.deserialize(config, settings, type.open(path));
			}
		}
	}

	private Path getFilePath(Config config) {
		Path dir = getConfigDir();

		String namespace = config.getNamespace();
		String name = config.getSaveName();

		return (namespace == null) ? dir.resolve(name) :  dir.resolve(namespace).resolve(name);
	}
}
