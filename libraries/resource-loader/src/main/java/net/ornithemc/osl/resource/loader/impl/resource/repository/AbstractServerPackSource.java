package net.ornithemc.osl.resource.loader.impl.resource.repository;

import java.util.function.Consumer;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.api.resource.repository.ServerPackSource;

public abstract class AbstractServerPackSource implements ServerPackSource {

	private ResourcePack defaultPack;

	protected AbstractServerPackSource() {
		if (!this.getClass().getName().equals("net.ornithemc.osl.resource.loader.impl.ServerResourcePacks")) {
			throw new IllegalStateException("naughty!");
		}
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		SimpleResourcePackRepository.serverPackSource = this;

		this.defaultPack = this.wrapOrCreateDefaultPack();

		ResourcePackSummary summary = ResourcePackSummary.create(
			this.defaultPack,
			false,
			false,
			PackPosition.BOTTOM
		);

		consumer.accept(summary);
	}

	@Override
	public ResourcePack getDefaultResourcePack() {
		return this.defaultPack;
	}

	protected abstract ResourcePack wrapOrCreateDefaultPack();

	public void close() {
		SimpleResourcePackRepository.serverPackSource = null;
	}
}
