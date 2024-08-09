package net.ornithemc.osl.config.api.config.option;

public interface Option {

	/**
	 * Returns the display name of this option. This can be a raw string or a
	 * translation key.
	 */
	String getName();

	/**
	 * Returns the description of this option. This can be a raw string or a
	 * translation key.
	 * 
	 * Can be {@code null}.
	 */
	String getDescription();

	/**
	 * Returns whether this option is currently set to its default value.
	 */
	boolean isDefault();

	/**
	 * Reset this option to its default value.
	 */
	void reset();

	void load();

	void unload();

}
