package net.ornithemc.osl.resource.loader.impl;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.AbstractClientPackSource;

public class ClientResourcePacks extends AbstractClientPackSource {

	private final ResourcePack defaultPack;

	public ClientResourcePacks() {
		this.defaultPack = new DefaultTexturePack();
	}

	@Override
	protected ResourcePack getOrWrapDefaultPack() {
		return this.defaultPack;
	}

	@Override
	protected ResourcePack getOrWrapServerPack() {
		return null;
	}
}
