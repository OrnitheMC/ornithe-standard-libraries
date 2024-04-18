package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class CharacterOption extends BaseOption<Character> {

	public CharacterOption(String name, String description, Character defaultValue) {
		super(name, description, defaultValue);
	}

	public CharacterOption(String name, String description, Character defaultValue, Predicate<Character> validator) {
		super(name, description, defaultValue, validator);
	}
}
