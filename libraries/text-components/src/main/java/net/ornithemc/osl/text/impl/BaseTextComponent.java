package net.ornithemc.osl.text.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.Style;
import net.ornithemc.osl.text.api.StyledTextVisitor;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;
import net.ornithemc.osl.text.api.TextVisitResults;
import net.ornithemc.osl.text.api.TextVisitor;

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
		StringBuilder sb = new StringBuilder();

		this.visit(text -> {
			sb.append(text);
			return TextVisitResults.CONTINUE;
		});

		return sb.toString();
	}

	@Override
	public String buildFormattedString() {
		StringBuilder sb = new StringBuilder();

		this.visit(Style.EMPTY, (style, text) -> {
			style.apply(sb);
			sb.append(text);
			return TextVisitResults.CONTINUE;
		});

		return sb.toString();
	}

	@Override
	public <T> Optional<T> visit(TextVisitor<T> visitor) {
		Optional<T> result = this.visitSelf(visitor);
		if (result.isPresent()) {
			return result;
		}

		for (TextComponent sibling : this.siblings) {
			result = sibling.visit(visitor);
			if (result.isPresent()) {
				return result;
			}
		}

		return Optional.empty();
	}

	abstract <T> Optional<T> visitSelf(TextVisitor<T> visitor);

	@Override
	public <T> Optional<T> visit(Style fallback, StyledTextVisitor<T> visitor) {
		Style style = fallback.withStyle(this.style);

		Optional<T> result = this.visitSelf(style, visitor);
		if (result.isPresent()) {
			return result;
		}

		for (TextComponent sibling : this.siblings) {
			result = sibling.visit(style, visitor);
			if (result.isPresent()) {
				return result;
			}
		}

		return Optional.empty();
	}

	abstract <T> Optional<T> visitSelf(Style style, StyledTextVisitor<T> visitor);

	@Override
	public TextComponent copy() {
		BaseTextComponent copy = this.copySelf();

		copy.siblings.addAll(this.siblings);
		copy.style = this.style;

		return copy;
	}

	abstract BaseTextComponent copySelf();

	@Override
	public TextComponent deepCopy() {
		TextComponent copy = this.copy().format(this.style);

		for (TextComponent sibling : this.siblings) {
			copy.append(sibling.deepCopy());
		}

		return copy;
	}
}
