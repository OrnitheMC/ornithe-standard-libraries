package net.ornithemc.osl.config.api.config;

import java.util.Collection;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;
import net.ornithemc.osl.config.api.serdes.FileSerializerType;

public interface Config {

	/**
	 * Returns this config's namespace.
	 * Namespaces group related configs together. Each namespace gets its own
	 * directory within the config directory of each scope. Typically each mod
	 * should use its mod id as the namespace for its configs.
	 */
	String getNamespace();

	/**
	 * Returns the display name for this config. This can be a raw string or a
	 * translation key.
	 */
	String getName();

	/**
	 * Returns the name of the file this config is saved to.
	 */
	String getSaveName();

	/**
	 * Returns the scope of this config.
	 * 
	 * @see ConfigScope
	 */
	ConfigScope getScope();

	/**
	 * Returns the loading phase of this config.
	 * 
	 * @see LoadingPhase
	 */
	LoadingPhase getLoadingPhase();

	/**
	 * Returns the serializer type used to save this config to file.
	 * 
	 * @see net.ornithemc.osl.config.api.serdes.SerializerTypes SerializerTypes
	 */
	FileSerializerType<?> getType();

	int getVersion();

	/**
	 * This method is called after your config has been successfully registered to
	 * the config manager. Use it to register your options and otherwise initialize
	 * your config.
	 */
	void init();

	void load();

	void unload();

	/**
	 * Returns all option groups in this config.
	 */
	Collection<OptionGroup> getGroups();

	/**
	 * Returns the option group in this config with the given name, or {@code null}
	 * if it does not exist.
	 */
	OptionGroup getGroup(String name);

	/**
	 * Reset all options in this config to their default values.
	 */
	void resetAll();

}
