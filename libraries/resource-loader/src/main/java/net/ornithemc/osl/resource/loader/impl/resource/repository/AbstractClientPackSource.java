package net.ornithemc.osl.resource.loader.impl.resource.repository;

import java.util.function.Consumer;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ClientPackSource;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

public abstract class AbstractClientPackSource implements ClientPackSource {

	private ResourcePack defaultPack;
	private ResourcePack serverPack;

	protected AbstractClientPackSource() {
		if (!this.getClass().getName().equals("net.ornithemc.osl.resource.loader.impl.ClientResourcePacks")) {
			throw new IllegalStateException("naughty!");
		}
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		SimpleResourcePackRepository.clientPackSource = this;

		this.defaultPack = this.getOrWrapDefaultPack();
		this.serverPack = this.getOrWrapServerPack();

		ResourcePackSummary summary = ResourcePackSummary.create(
			this.defaultPack,
			DEFAULT_PACK_ID,
			true,
			false,
			PackPosition.BOTTOM
		);

		consumer.accept(summary);

		if (this.serverPack != null) {
			ResourcePackSummary serverSummary = ResourcePackSummary.create(
				this.serverPack,
				SERVER_PACK_ID,
				true,
				true,
				PackPosition.TOP
			);

			if (serverSummary != null) {
				consumer.accept(serverSummary);
			}
		}
	}

	@Override
	public ResourcePack getDefaultResourcePack() {
		return this.defaultPack;
	}

	@Override
	public ResourcePack getServerResourcePack() {
		return this.serverPack;
	}

	protected abstract ResourcePack getOrWrapDefaultPack();

	protected abstract ResourcePack getOrWrapServerPack();

	public void close() {
		SimpleResourcePackRepository.clientPackSource = null;
	}
}
