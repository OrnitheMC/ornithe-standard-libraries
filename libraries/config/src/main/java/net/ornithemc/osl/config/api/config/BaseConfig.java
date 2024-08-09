package net.ornithemc.osl.config.api.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;

public abstract class BaseConfig implements Config {

	private final Map<String, OptionGroup> groups;

	private boolean loaded;

	protected BaseConfig() {
		this.groups = new LinkedHashMap<>();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
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

	public final void requireLoaded() {
		if (!loaded) {
			throw new IllegalStateException("this config is not loaded!");
		}
	}

	protected final void registerOptions(String group, Option... options) {
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
