package net.ornithemc.osl.text.impl.access;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Formatting;
import net.minecraft.text.HoverEvent;

public interface StyleAccess {

	Formatting osl$text$color();

	Boolean osl$text$bold();

	Boolean osl$text$italic();

	Boolean osl$text$underlined();

	Boolean osl$text$strikethrough();

	Boolean osl$text$obfuscated();

	ClickEvent osl$text$clickEvent();

	HoverEvent osl$text$hoverEvent();

	default String osl$text$insertion() {
		throw new UnsupportedOperationException("allowed in 14w02a+ only");
	}
}
