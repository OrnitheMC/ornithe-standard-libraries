package net.ornithemc.osl.resource.loader.impl;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.AbstractServerPackSource;

public class ServerResourcePacks extends AbstractServerPackSource {

	private ResourcePack defaultPack;

	public ServerResourcePacks() {
		this.defaultPack = new DefaultDataPack();
	}

	@Override
	protected ResourcePack getOrWrapDefaultPack() {
		return this.defaultPack;
	}
}
