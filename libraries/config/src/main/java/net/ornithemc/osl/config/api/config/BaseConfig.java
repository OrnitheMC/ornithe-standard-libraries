package net.ornithemc.osl.config.api.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;
import net.ornithemc.osl.config.api.serdes.FileSerializerType;
import net.ornithemc.osl.config.api.serdes.SerializerTypes;

public abstract class BaseConfig implements Config {

	protected final String namespace;
	protected final String name;
	protected final String saveName;
	protected final ConfigScope scope;
	protected final LoadingPhase phase;

	private final Map<String, OptionGroup> groups;

	private boolean loaded;

	public BaseConfig(String name, String saveName, ConfigScope scope, LoadingPhase phase) {
		this(null, name, saveName, scope, phase);
	}

	public BaseConfig(String namespace, String name, String saveName, ConfigScope scope, LoadingPhase phase) {
		if (namespace == null) {
			Class<? extends BaseConfig> type = getClass();
			Optional<ModContainer> mod = QuiltLoader.getModContainer(type);

			if (mod.isPresent()) {
				namespace = mod.get().metadata().id();
			}
		}

		this.namespace = namespace;
		this.name = name;
		this.saveName = saveName;
		this.scope = scope;
		this.phase = phase;

		this.groups = new LinkedHashMap<>();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSaveName() {
		return saveName;
	}

	@Override
	public ConfigScope getScope() {
		return scope;
	}

	@Override
	public LoadingPhase getLoadingPhase() {
		return phase;
	}

	@Override
	public FileSerializerType<?> getType() {
		return SerializerTypes.JSON;
	}

	@Override
	public Collection<OptionGroup> getGroups() {
		return groups.values();
	}

	@Override
	public OptionGroup getGroup(String name) {
		return groups.get(name);
	}

	@Override
	public void resetAll() {
		requireLoaded();

		for (OptionGroup group : getGroups()) {
			group.resetAll();
		}
	}

	@Override
	public void load() {
		loaded = true;

		for (OptionGroup group : getGroups()) {
			group.load();
		}
	}

	@Override
	public void unload() {
		for (OptionGroup group : getGroups()) {
			group.unload();
		}

		loaded = false;
	}

	public void requireLoaded() {
		if (!loaded) {
			throw new IllegalStateException("this config is not loaded!");
		}
	}

	protected void registerOptions(String group, Option... options) {
		if (loaded) {
			throw new IllegalStateException("cannot register new options while this config is loaded!");
		}
		if (options.length == 0) {
			return; // weird but okay
		}

		OptionGroup optionGroup = groups.get(group);

		if (optionGroup == null) {
			optionGroup = new OptionGroup(group);
			groups.put(group, optionGroup);
		}

		optionGroup.registerOptions(options);
	}
}
