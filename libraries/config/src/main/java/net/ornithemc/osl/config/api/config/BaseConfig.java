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

public abstract class BaseConfig implements Config {

	protected final String namespace;
	protected final String name;
	protected final String saveName;
	protected final ConfigScope scope;
	protected final LoadingPhase phase;

	private final Map<String, OptionGroup> groups;

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
	public Collection<OptionGroup> getGroups() {
		return groups.values();
	}

	@Override
	public void resetAll() {
		for (OptionGroup group : getGroups()) {
			group.resetAll();
		}
	}

	@Override
	public void load() {
		// TODO
	}

	@Override
	public void unload() {
		// TODO
	}

	protected void registerOptions(String group, Option... options) {
		// TODO
	}
}
