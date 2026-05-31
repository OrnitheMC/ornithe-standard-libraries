package net.ornithemc.osl.resource.loader.impl;

import net.minecraft.client.resource.pack.ResourcePacks;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.adapter.WrappedResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.AbstractClientPackSource;

public class ClientResourcePacks extends AbstractClientPackSource {

	private final ResourcePacks resourcePacks;

	public ClientResourcePacks(ResourcePacks resourcePacks) {
		this.resourcePacks = resourcePacks;
	}

	@Override
	protected ResourcePack getOrWrapDefaultPack() {
		return new WrappedResourcePack(this.resourcePacks.defaultPack);
	}

	@Override
	protected ResourcePack getOrWrapServerPack() {
		if (this.resourcePacks.getServerPack() != null) {
			return new WrappedResourcePack(this.resourcePacks.getServerPack());
		} else {
			return null;
		}
	}
}
