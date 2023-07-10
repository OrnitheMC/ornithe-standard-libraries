package net.ornithemc.osl.config.api.config.option;

import java.nio.file.Path;
import java.util.function.Predicate;

public class PathOption extends BaseOption<Path> {

	public PathOption(String name, String description, Path defaultValue) {
		super(name, description, defaultValue);
	}

	public PathOption(String name, String description, Path defaultValue, Predicate<Path> validator) {
		super(name, description, defaultValue, validator);
	}
}
