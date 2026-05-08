package net.ornithemc.osl.resource.loader.impl;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.access.ResourcePacksAccess;
import net.ornithemc.osl.resource.loader.impl.adapter.WrappedPack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.AbstractClientPackSource;

public class ClientResourcePacks extends AbstractClientPackSource {

	private final net.minecraft.client.resource.pack.ResourcePacks resourcePacks;

	public ClientResourcePacks(net.minecraft.client.resource.pack.ResourcePacks resourcePacks) {
		this.resourcePacks = resourcePacks;
	}

	@Override
	protected ResourcePack getOrWrapDefaultPack() {
		return new WrappedPack(this.resourcePacks.getDefaultPack());
	}

	@Override
	protected ResourcePack getOrWrapServerPack() {
		if (((ResourcePacksAccess) this.resourcePacks).osl$resource_loader$getServerPack() != null) {
			return new WrappedPack(((ResourcePacksAccess) this.resourcePacks).osl$resource_loader$getServerPack()); // TODO
		} else {
			return null;
		}
	}
}
