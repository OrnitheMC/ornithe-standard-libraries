package net.ornithemc.osl.text.impl;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.text.api.ClickEvent;
import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.HoverEvent;
import net.ornithemc.osl.text.api.Style;
import net.ornithemc.osl.text.api.TextColor;
import net.ornithemc.osl.text.api.TextComponents;
import net.ornithemc.osl.text.impl.access.StyleAccess;

public final class StyleResolver {

	public static final boolean INSERTION_EXISTS = MinecraftVersion.resolve().compareTo("14w02a") >= 0;

	public static Style resolve(net.minecraft.text.Style style) {
		StyleAccess s = (StyleAccess) style;

		return Style.EMPTY
			.withColor(resolveTextColor(s.osl$text$color()))
			.withBold(s.osl$text$bold())
			.withItalic(s.osl$text$italic())
			.withUnderlined(s.osl$text$underlined())
			.withStrikethrough(s.osl$text$strikethrough())
			.withObfuscated(s.osl$text$obfuscated())
			.withClickEvent(resolveClickEvent(s.osl$text$clickEvent()))
			.withHoverEvent(resolveHoverEvent(s.osl$text$hoverEvent()))
			.withInsertion(INSERTION_EXISTS ? s.osl$text$insertion() : null);
	}

	public static TextColor resolveTextColor(net.minecraft.text.Formatting color) {
		if (color == null) {
			return null;
		}

		return TextColor.fromFormatting(Formatting.byCode(color.toString().charAt(1)));
	}

	public static ClickEvent resolveClickEvent(net.minecraft.text.ClickEvent event) {
		if (event == null) {
			return null;
		}

		switch (event.getAction()) {
		case OPEN_URL:
			return ClickEvent.openUrl(event.getValue());
		case RUN_COMMAND:
			return ClickEvent.runCommand(event.getValue());
		case SUGGEST_COMMAND:
			return ClickEvent.suggestCommand(event.getValue());
		default:
			return null; // TODO
		}
	}

	public static HoverEvent resolveHoverEvent(net.minecraft.text.HoverEvent event) {
		if (event == null) {
			return null;
		}

		switch (event.getAction()) {
		case SHOW_TEXT:
			return HoverEvent.showText(TextComponents.resolve(event.getValue()));
		default:
			return null; // TODO
		}
	}
}
