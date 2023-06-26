package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

import net.minecraft.resource.Identifier;

public class IdentifierOption extends BaseOption<Identifier> {

	public IdentifierOption(String name, String description, Identifier defaultValue) {
		super(name, description, defaultValue);
	}

	public IdentifierOption(String name, String description, Identifier defaultValue, Predicate<Identifier> validator) {
		super(name, description, defaultValue, validator);
	}
}
