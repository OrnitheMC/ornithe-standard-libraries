package net.ornithemc.osl.resource.loader.impl;

import java.util.function.Consumer;

import net.minecraft.client.resource.pack.TexturePack;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.impl.adapter.WrappedTexturePack;

public class ClientResourcePacks implements ResourcePackRepository.Source {

	private final TexturePack defaultPack;

	public ClientResourcePacks(TexturePack defaultPack) {
		this.defaultPack = defaultPack;
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		ResourcePack pack = new WrappedTexturePack(this.defaultPack);
		ResourcePackSummary summary = ResourcePackSummary.create(
			pack,
			true,
			false,
			PackPosition.BOTTOM
		);

		consumer.accept(summary);
	}
}
