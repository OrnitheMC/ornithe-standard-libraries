package net.ornithemc.osl.text.impl;

import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;
import net.ornithemc.osl.text.api.TextComponent.Builder;

public class TextComponentBuilder implements TextComponent.Builder {

	private TextComponent result;

	private TextComponent result() {
		if (this.result == null) {
			this.result = TextComponents.literal("");
		}

		return this.result;
	}

	@Override
	public Builder append(String text) {
		this.result().append(text);
		return this;
	}

	@Override
	public Builder append(TextComponent text) {
		this.result().append(text);
		return this;
	}

	@Override
	public TextComponent build() {
		return this.result();
	}
}
