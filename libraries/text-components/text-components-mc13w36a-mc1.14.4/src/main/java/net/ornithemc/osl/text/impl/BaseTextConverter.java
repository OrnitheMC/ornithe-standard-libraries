package net.ornithemc.osl.text.impl;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import net.ornithemc.osl.text.api.TextComponent;

public class BaseTextConverter implements TextConverter<Text> {

	@Override
	public Text convert(TextComponent component) {
		Text text;

		if (component instanceof LiteralTextComponent) {
			text = new LiteralText(((LiteralTextComponent) component).getValue());
		} else if (component instanceof TranslatableTextComponent) {
			text = new TranslatableText(((TranslatableTextComponent) component).getKey(), ((TranslatableTextComponent) component).getArgs());
		} else {
			text = new LiteralText("");
		}

		text.setStyle(StyleConverter.convert(component.getStyle()));

		for (TextComponent sibling : component.getSiblings()) {
			text.append(this.convert(sibling));
		}

		return text;
	}
}
