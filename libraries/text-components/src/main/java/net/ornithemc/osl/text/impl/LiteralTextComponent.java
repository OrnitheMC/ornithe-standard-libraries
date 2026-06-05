package net.ornithemc.osl.text.impl;

import java.util.Optional;

import net.ornithemc.osl.text.api.Style;
import net.ornithemc.osl.text.api.StyledTextVisitor;
import net.ornithemc.osl.text.api.TextVisitor;

public class LiteralTextComponent extends BaseTextComponent {

	private final String value;

	public LiteralTextComponent(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	<T> Optional<T> visitSelf(TextVisitor<T> visitor) {
		return visitor.accept(this.value);
	}

	@Override
	<T> Optional<T> visitSelf(Style style, StyledTextVisitor<T> visitor) {
		return visitor.accept(style, this.value);
	}

	@Override
	BaseTextComponent copySelf() {
		return new LiteralTextComponent(this.value);
	}
}
