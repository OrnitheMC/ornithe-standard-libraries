package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

public class ByteOption extends BaseOption<Byte> {

	public ByteOption(String name, String description, Byte defaultValue) {
		super(name, description, defaultValue);
	}

	public ByteOption(String name, String description, Byte defaultValue, Predicate<Byte> validator) {
		super(name, description, defaultValue, validator);
	}
}
