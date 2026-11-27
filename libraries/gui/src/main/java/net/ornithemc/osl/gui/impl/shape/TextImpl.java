package net.ornithemc.osl.gui.impl.shape;

import net.ornithemc.osl.gui.api.color.Color;
import net.ornithemc.osl.gui.api.shape.Text;
import net.ornithemc.osl.text.api.TextComponent;

public class TextImpl implements Text {
	private final TextComponent value;
	private final Color color;
	private final boolean isShaded;

	public TextImpl(TextComponent value, Color color, boolean isShaded) {
		this.value = value;
		this.color = color;
		this.isShaded = isShaded;
	}

	@Override
	public TextComponent value() {
		return this.value;
	}

	@Override
	public Color color() {
		return this.color;
	}

	@Override
	public boolean isShaded() {
		return this.isShaded;
	}
}
