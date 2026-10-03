package net.ornithemc.osl.resource.loader.impl.client;

import net.ornithemc.osl.resource.loader.api.client.MinecraftExtension;

public interface MinecraftExtensionImpl extends MinecraftExtension {

	@Override
	default void reloadResources() {
		throw new AbstractMethodError();
	}
}
