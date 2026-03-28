package net.ornithemc.osl.resource.loader.impl;

import java.util.function.Consumer;

import net.minecraft.client.resource.pack.ResourcePacks;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.impl.adapter.WrappedResourcePack;

public class ClientResourcePacks implements ResourcePackRepository.Source {

	private final ResourcePacks resourcePacks;

	public ClientResourcePacks(ResourcePacks resourcePacks) {
		this.resourcePacks = resourcePacks;
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		ResourcePack pack = new WrappedResourcePack(this.resourcePacks.defaultPack);
		ResourcePackSummary summary = ResourcePackSummary.create(
			pack,
			true,
			false,
			PackPosition.BOTTOM
		);

		consumer.accept(summary);

		if (this.resourcePacks.getServerPack() != null) {
			ResourcePack serverPack = new WrappedResourcePack(this.resourcePacks.getServerPack());
			ResourcePackSummary serverSummary = ResourcePackSummary.create(
				serverPack,
				true,
				true,
				PackPosition.TOP
			);

			if (serverSummary != null) {
				consumer.accept(serverSummary);
			}
		}
	}
}
