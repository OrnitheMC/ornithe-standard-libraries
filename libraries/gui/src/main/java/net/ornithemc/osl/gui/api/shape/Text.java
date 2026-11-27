package net.ornithemc.osl.gui.api.shape;

import net.ornithemc.osl.gui.api.color.Color;
import net.ornithemc.osl.gui.impl.shape.TextImpl;
import net.ornithemc.osl.text.api.TextComponent;

public interface Text {
	static Text of(TextComponent value, Color color, boolean isShaded) {
		return new TextImpl(value, color, isShaded);
	}

	static Text of(TextComponent value, Color color) {
		return of(value, color, true);
	}

	TextComponent value();

	Color color();

	boolean isShaded();
}
