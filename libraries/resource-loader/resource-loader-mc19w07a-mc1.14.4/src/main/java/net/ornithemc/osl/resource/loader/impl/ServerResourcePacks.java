package net.ornithemc.osl.resource.loader.impl;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.access.DataPacksAccess;
import net.ornithemc.osl.resource.loader.impl.adapter.WrappedPack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.AbstractServerPackSource;

public class ServerResourcePacks extends AbstractServerPackSource {

	private final net.minecraft.server.resource.pack.DataPacks dataPacks;

	public ServerResourcePacks(net.minecraft.server.resource.pack.DataPacks dataPacks) {
		this.dataPacks = dataPacks;
	}

	@Override
	protected ResourcePack getOrWrapDefaultPack() {
		return new WrappedPack(((DataPacksAccess) this.dataPacks).osl$resource_loader$getDefaultPack());
	}
}
