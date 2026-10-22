package net.ornithemc.osl.text.impl;

import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.Style;
import net.ornithemc.osl.text.api.TextComponent;
import net.minecraft.text.Text;
import net.ornithemc.osl.text.api.TextComponents;

import java.util.List;
import java.util.stream.Collectors;

public class BaseTextResolver implements TextResolver<Text> {
	@Override
	public TextComponent resolve(Text o) {
		List<Text> siblings = o.getSiblings();

		TextComponent component;
		if (o.getTranslated() != null) {
			if (siblings == null) {
				component = TextComponents.translatable(o.getTranslated());
			} else {
				List<TextComponent> args = siblings.stream().map(this::resolve).collect(Collectors.toList());
				component = TextComponents.translatable(o.getTranslated(), args.toArray());
			}
		} else if (o.getText() != null) {
			component = TextComponents.literal(o.getText());
		} else if (siblings != null) {
			component = TextComponents.composite(siblings.toArray());
		} else {
			component = TextComponents.literal("");
		}

		return component.format(Style.EMPTY
				.withColor(o.getColor() != null ? Formatting.byCode(o.getColor().getCode()) : null)
				.withBold(o.isBold())
				.withItalic(o.isItalic())
				.withUnderlined(o.isUnderlined())
				.withObfuscated(o.isObfuscated()));
	}
}
