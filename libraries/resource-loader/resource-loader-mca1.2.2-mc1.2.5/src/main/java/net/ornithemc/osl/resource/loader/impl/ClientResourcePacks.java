package net.ornithemc.osl.resource.loader.impl;

import net.minecraft.client.resource.pack.TexturePacks;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.access.TexturePacksAccess;
import net.ornithemc.osl.resource.loader.impl.adapter.WrappedTexturePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.AbstractClientPackSource;

public class ClientResourcePacks extends AbstractClientPackSource {

	private final TexturePacks texturePacks;

	public ClientResourcePacks(TexturePacks texturePacks) {
		this.texturePacks = texturePacks;
	}

	@Override
	protected ResourcePack getOrWrapDefaultPack() {
		return new WrappedTexturePack(((TexturePacksAccess) this.texturePacks).osl$resource_loader$getDefaultPack());
	}

	@Override
	protected ResourcePack getOrWrapServerPack() {
		return null;
	}
}
