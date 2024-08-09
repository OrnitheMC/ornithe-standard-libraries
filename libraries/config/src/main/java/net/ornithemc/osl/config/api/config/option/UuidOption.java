package net.ornithemc.osl.config.api.config.option;

import java.util.UUID;
import java.util.function.Predicate;

public class UuidOption extends BaseOption<UUID> {

	public UuidOption(String name, String description, UUID defaultValue, Predicate<UUID> validator) {
		super(name, description, defaultValue, validator);
	}

	public UuidOption(String name, String description, UUID defaultValue) {
		super(name, description, defaultValue);
	}
}
