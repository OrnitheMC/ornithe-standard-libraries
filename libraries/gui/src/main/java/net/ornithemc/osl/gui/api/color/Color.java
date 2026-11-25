package net.ornithemc.osl.gui.api.color;

public interface Color {
	int red();

	int green();

	int blue();

	double alpha();

	int hexNoAlpha();

	int hexWithAlpha();

	Color withRed(int r);

	Color withGreen(int g);

	Color withBlue(int b);

	Color withAlpha(double a);
}
