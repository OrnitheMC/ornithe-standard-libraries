package net.ornithemc.osl.text.impl;

import net.ornithemc.osl.text.api.Style;
import net.ornithemc.osl.text.api.TextComponent;
import net.minecraft.text.Text;

public class BaseTextConverter implements TextConverter<Text> {

	@Override
	public Text convert(TextComponent component) {
		Text text;

		if (component instanceof LiteralTextComponent) {
			text = Text.literal(((LiteralTextComponent) component).getValue());
		} else if (component instanceof TranslatableTextComponent) {
			text = Text.translatable(((TranslatableTextComponent) component).getKey(), ((TranslatableTextComponent) component).getArgs());
		} else {
			text = Text.literal("");
		}

		Style style = component.getStyle();

		if (style.getColor() != null && style.getColor().isFormatting())
			text.setColor(net.minecraft.text.Formatting.byName(style.getColor().serialize()));
		if (style.withBold(style.isBold()) != style)
			text.setBold(style.isBold());
		if (style.withItalic(style.isItalic()) != style)
			text.setItalic(style.isItalic());
		if (style.withUnderlined(style.isUnderlined()) != style)
			text.setUnderlined(style.isUnderlined());
		if (style.withObfuscated(style.isObfuscated()) != style)
			text.setObfuscated(style.isObfuscated());

		for (TextComponent sibling : component.getSiblings()) {
			text.append(this.convert(sibling));
		}

		return text;
	}
}
