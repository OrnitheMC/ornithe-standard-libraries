package net.ornithemc.osl.text.impl;

import java.util.function.Function;

import net.minecraft.text.BaseText;
import net.minecraft.text.Text;

import net.ornithemc.osl.text.api.Style;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class BaseTextResolver<T extends BaseText> implements TextResolver<T> {

	private final Function<T, TextComponent> builder;

	public BaseTextResolver(Function<T, TextComponent> builder) {
		this.builder = builder;
	}

	@Override
	public TextComponent resolve(T t) {
		TextComponent text = this.builder.apply(t);
		Style style = StyleResolver.resolve(t.getStyle());

		text.format(style);

		for (Text sibling : t.getSiblings()) {
			text.append(TextComponents.resolve(sibling));
		}

		return text;
	}
}
