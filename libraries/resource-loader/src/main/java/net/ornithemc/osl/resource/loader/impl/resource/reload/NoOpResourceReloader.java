package net.ornithemc.osl.resource.loader.impl.resource.reload;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;

public enum NoOpResourceReloader implements ResourceReloadListener  {

	INSTANCE;

	@Override
	public void resourcesReloaded(ResourceManager manager) {
	}
}
