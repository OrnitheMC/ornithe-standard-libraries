package net.ornithemc.osl.resource.loader.impl.adapter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.resource.pack.repository.RepositorySource;
import net.minecraft.resource.pack.repository.UnopenedPack;

import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

class WrappedRepositorySource<T extends UnopenedPack> implements ResourcePackRepository.Source {

	private final UnopenedPack.Factory<T> factory;
	private final RepositorySource source;

	WrappedRepositorySource(UnopenedPack.Factory<T> factory, RepositorySource source) {
		this.factory = factory;
		this.source = source;
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		Map<String, T> packs = new LinkedHashMap<>();
		this.source.loadPacks(packs, this.factory);
		packs.values().forEach(pack -> {
			consumer.accept(Adapters.resourcePackSummary(pack));
		});
	}
}
