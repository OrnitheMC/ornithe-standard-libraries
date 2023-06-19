package net.ornithemc.osl.config.api.config.option.group;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.ornithemc.osl.config.api.config.option.Option;

public class OptionGroup {

	private final String name;
	private final Map<String, Option<?>> options;

	public OptionGroup(String name) {
		this.name = name;
		this.options = new LinkedHashMap<>();
	}

	public String getName() {
		return name;
	}

	public Collection<Option<?>> getOptions() {
		return options.values();
	}

	public void resetAll() {
		for (Option<?> option : getOptions()) {
			option.reset();
		}
	}
}
