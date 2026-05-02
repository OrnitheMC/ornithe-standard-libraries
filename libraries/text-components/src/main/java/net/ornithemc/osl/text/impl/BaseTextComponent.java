package net.ornithemc.osl.text.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.Style;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

abstract class BaseTextComponent implements TextComponent {

	private final List<TextComponent> siblings;

	Style style;

	BaseTextComponent() {
		this.siblings = new ArrayList<>();

		this.style = Style.EMPTY;
	}

	@Override
	public Style getStyle() {
		return this.style;
	}

	@Override
	public List<TextComponent> getSiblings() {
		return Collections.unmodifiableList(this.siblings);
	}

	@Override
	public TextComponent format(Formatting... formattings) {
		this.style = this.style.withFormatting(formattings);
		return this;
	}

	@Override
	public TextComponent format(Style style) {
		this.style = this.style.withStyle(style);
		return this;
	}

	@Override
	public TextComponent format(UnaryOperator<Style> styler) {
		this.style = styler.apply(this.style);
		return this;
	}

	@Override
	public TextComponent append(String text) {
		this.siblings.add(TextComponents.literal(text));
		return this;
	}

	@Override
	public TextComponent append(TextComponent text) {
		this.siblings.add(text);
		return this;
	}

	@Override
	public String buildString() {
		return this.buildString(false);
	}

	@Override
	public String buildFormattedString() {
		return this.buildString(true);
	}

	private String buildString(boolean formatted) {
		StringBuilder sb = new StringBuilder();

		this.buildString(sb, formatted);
		this.buildString(sb, formatted, this.siblings);

		if (formatted) {
			sb.append(Formatting.RESET);
		}

		return sb.toString();
	}

	abstract void buildString(StringBuilder sb, boolean formatted);

	final void buildString(StringBuilder sb, boolean formatted, List<TextComponent> texts) {
		for (TextComponent text : texts) {
			if (formatted) {
				this.style.apply(sb);
			}

			sb.append(formatted
				? text.buildFormattedString()
				: text.buildString());
		}
	}
}
