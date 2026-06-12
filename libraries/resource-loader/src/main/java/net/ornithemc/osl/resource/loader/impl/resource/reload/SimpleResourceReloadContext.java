package net.ornithemc.osl.resource.loader.impl.resource.reload;

import java.util.List;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadContext;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;

public class SimpleResourceReloadContext implements ResourceReloadContext {

	private final List<ResourcePack> resourcePacks;
	private final List<ResourceReloader> resourceReloaders;

	public SimpleResourceReloadContext(List<ResourcePack> resourcePacks, List<ResourceReloader> resourceReloaders) {
		this.resourcePacks = resourcePacks;
		this.resourceReloaders = resourceReloaders;
	}

	@Override
	public List<ResourcePack> resourcePacks() {
		return this.resourcePacks;
	}

	@Override
	public List<ResourceReloader> resourceReloaders() {
		return this.resourceReloaders;
	}
}
