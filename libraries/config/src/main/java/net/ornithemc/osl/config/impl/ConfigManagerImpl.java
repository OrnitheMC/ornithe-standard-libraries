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

public abstract class ConfigManagerImpl {

	private final ConfigScope scope;
	private final Path rootDir;

	private final Map<String, Config> configs;

	public ConfigManagerImpl(ConfigScope scope, Path rootDir) {
		this.scope = scope;
		this.rootDir = rootDir;

		this.configs = new LinkedHashMap<>();
	}

	public ConfigScope getScope() {
		return scope;
	}

	public Path getConfigDir() {
		return rootDir.resolve(scope.getPath());
	}

	public void register(Config config) {
		configs.compute(key(config.getNamespace(), config.getName()), (key, value) -> {
			if (value != null) {
				if (value == config) {
					return value; // odd thing to register a config multiple times, but okay
				} else {
					throw new IllegalStateException("config " + key + " is not unique");
				}
			}

			return config;
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
			}
		}
	}

	public void unload() {
		for (Config config : getConfigs()) {
			config.unload();
		}
	}

	public void save() {
		SerializationSettings settings = new SerializationSettings(); // TODO

		for (Config config : getConfigs()) {
			FileSerializerType<?> type = config.getType();

			try {
				save(config, settings, type);
			} catch (IOException e) {
				// TODO
			}
		}
	}

	private <M> void save(Config config, SerializationSettings settings, FileSerializerType<M> type) throws IOException {
		ConfigSerializer<M> serializer = ConfigSerializers.get(type);

		if (serializer == null) {
			throw new IOException("don't know how to serialize " + config + " to file!");
		} else {
			Path path = getFilePath(config);
			M medium = type.open(path);

			if (!Files.exists(path)) {
				Files.createDirectories(path.getParent());
			}

			serializer.serialize(config, settings, medium);
		}
	}

	private Path getFilePath(Config config) {
		Path dir = getConfigDir();

		String namespace = config.getNamespace();
		String name = config.getSaveName();

		return (namespace == null) ? dir.resolve(name) :  dir.resolve(namespace).resolve(name);
	}
}
