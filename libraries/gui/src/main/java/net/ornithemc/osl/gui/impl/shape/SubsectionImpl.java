package net.ornithemc.osl.gui.impl.shape;

import net.ornithemc.osl.gui.api.shape.texture.Subsection;

public class SubsectionImpl implements Subsection {
	private final int startX;
	private final int startY;
	private final int width;
	private final int height;

	public SubsectionImpl(int startX, int startY, int width, int height) {
		this.startX = startX;
		this.startY = startY;
		this.width = width;
		this.height = height;
	}

	@Override
	public int startX() {
		return this.startX;
	}

	@Override
	public int startY() {
		return this.startY;
	}

	@Override
	public int width() {
		return this.width;
	}

	@Override
	public int height() {
		return this.height;
	}
}
