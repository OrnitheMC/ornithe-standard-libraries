package net.ornithemc.osl.config.api.config.option.group;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.ornithemc.osl.config.api.config.option.Option;

public class OptionGroup {

	private final String name;
	private final Map<String, Option> options;

	private boolean loaded;

	public OptionGroup(String name) {
		this.name = name;
		this.options = new LinkedHashMap<>();
	}

	public String getName() {
		return name;
	}

	public Collection<Option> getOptions() {
		return options.values();
	}

	public Option getOption(String name) {
		return options.get(name);
	}

	public void resetAll() {
		requireLoaded();

		for (Option option : getOptions()) {
			option.reset();
		}
	}

	public void load() {
		loaded = true;

		for (Option option : getOptions()) {
			option.load();
		}
	}

	public void unload() {
		for (Option option : getOptions()) {
			option.unload();
		}

		loaded = false;
	}

	public void requireLoaded() {
		if (!loaded) {
			throw new IllegalStateException("this option group is not loaded!");
		}
	}
}
