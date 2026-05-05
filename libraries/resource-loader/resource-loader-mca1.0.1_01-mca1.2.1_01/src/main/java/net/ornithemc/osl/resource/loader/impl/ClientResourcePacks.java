package net.ornithemc.osl.resource.loader.impl;

import java.util.function.Consumer;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

public class ClientResourcePacks implements ResourcePackRepository.Source {

	private final ResourcePack defaultPack;

	public ClientResourcePacks() {
		this.defaultPack = new DefaultTexturePack();
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		ResourcePackSummary summary = ResourcePackSummary.create(
			this.defaultPack,
			true,
			false,
			PackPosition.BOTTOM
		);

		consumer.accept(summary);
	}
}
