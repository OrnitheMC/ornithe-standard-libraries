package net.ornithemc.osl.gui.api.shape.texture;

import net.ornithemc.osl.gui.impl.shape.SubsectionImpl;

public interface Subsection {
	static Subsection of(int startX, int startY, int width, int height) {
		return new SubsectionImpl(startX, startY, width, height);
	}

	int startX();

	int startY();

	int width();

	int height();
}
