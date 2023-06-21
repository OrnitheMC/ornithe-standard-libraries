package net.ornithemc.osl.config.api.config;

import java.util.Collection;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;
import net.ornithemc.osl.config.api.serdes.FileSerializerType;

public interface Config {

	String getNamespace();

	String getName();

	String getSaveName();

	ConfigScope getScope();

	LoadingPhase getLoadingPhase();

	FileSerializerType<?> getType();

	int getVersion();

	void load();

	void unload();

	Collection<OptionGroup> getGroups();

	void resetAll();

}
