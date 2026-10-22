package net.ornithemc.osl.text.impl;

import net.minecraft.text.Text;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.text.api.ClickEvent;
import net.ornithemc.osl.text.api.HoverEvent;
import net.ornithemc.osl.text.api.Style;
import net.ornithemc.osl.text.api.TextColor;
import net.ornithemc.osl.text.api.TextComponents;

public final class StyleConverter {

	public static final boolean INSERTION_EXISTS = MinecraftVersion.resolve().compareTo("14w02a") >= 0;

	public static net.minecraft.text.Style convert(Style style) {
		net.minecraft.text.Style s = new net.minecraft.text.Style();

		if (style.getColor() != null)
			s.setColor(convertTextColor(style.getColor()));
		if (style.withBold(style.isBold()) != style)
			s.setBold(style.isBold());
		if (style.withItalic(style.isItalic()) != style)
			s.setItalic(style.isItalic());
		if (style.withUnderlined(style.isUnderlined()) != style)
			s.setUnderlined(style.isUnderlined());
		if (style.withStrikethrough(style.isStrikethrough()) != style)
			s.setStrikethrough(style.isStrikethrough());
		if (style.withObfuscated(style.isObfuscated()) != style)
			s.setObfuscated(style.isObfuscated());
		if (style.getClickEvent() != null)
			s.setClickEvent(convertClickEvent(style.getClickEvent()));
		if (style.getHoverEvent() != null)
			s.setHoverEvent(convertHoverEvent(style.getHoverEvent()));
		if (INSERTION_EXISTS && style.getInsertion() != null)
			s.setInsertion(style.getInsertion());

		return s;
	}

	public static net.minecraft.text.Formatting convertTextColor(TextColor color) {
		if (!color.isFormatting()) {
			return null;
		}

		return net.minecraft.text.Formatting.valueOf(color.getFormatting().name());
	}

	public static net.minecraft.text.ClickEvent convertClickEvent(ClickEvent event) {
		switch (event.getAction()) {
		case OPEN_URL:
			return new net.minecraft.text.ClickEvent(net.minecraft.text.ClickEvent.Action.OPEN_URL, event.getValue());
		case RUN_COMMAND:
			return new net.minecraft.text.ClickEvent(net.minecraft.text.ClickEvent.Action.RUN_COMMAND, event.getValue());
		case SUGGEST_COMMAND:
			return new net.minecraft.text.ClickEvent(net.minecraft.text.ClickEvent.Action.SUGGEST_COMMAND, event.getValue());
		default:
			return null; // TODO
		}
	}

	public static net.minecraft.text.HoverEvent convertHoverEvent(HoverEvent event) {
		switch (event.getAction()) {
		case SHOW_TEXT:
			return new net.minecraft.text.HoverEvent(net.minecraft.text.HoverEvent.Action.SHOW_TEXT, TextComponents.convert(event.getValue(), Text.class));
		default:
			return null; // TODO
		}
	}
}
