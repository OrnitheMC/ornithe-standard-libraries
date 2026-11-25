package net.ornithemc.osl.gui.api;

/**
 * A collection of transforms on rendered things that are reverted after they are no longer necessary.
 */
public interface Transforms extends AutoCloseable {
	/**
	 * Reverts all transformations done, so that regular rendering may resume.
	 */
	void close();

	/**
	 *
	 * @param x amount to horizontally translate screen rendering by
	 * @param y amount to vertically translate screen rendering by
	 */
	void translate(double x, double y);

	void enableBlend();
}
