package net.ornithemc.osl.resource.loader.impl.adapter;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.minecraft.resource.pack.repository.PackRepository;
import net.minecraft.resource.pack.repository.RepositorySource;
import net.minecraft.resource.pack.repository.UnopenedPack;
import net.minecraft.resource.pack.repository.UnopenedPack.Factory;

import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

public class ResourcePackRepositoryAdapter<T extends UnopenedPack> extends PackRepository<T> {

	private final UnopenedPack.Factory<T> factory;
	private final Map<String, T> unopenedPacks;

	private final ResourcePackRepository packRepository;

	public ResourcePackRepositoryAdapter(Factory<T> factory, ResourcePackRepository repository) {
		super(factory);

		this.factory = factory;
		this.unopenedPacks = new HashMap<>();

		this.packRepository = repository;
	}

	@Override
	public void reload() {
		this.unopenedPacks.clear();
		this.packRepository.reload();

		for (ResourcePackSummary summary : this.packRepository.getAvailablePacks()) {
			this.unopenedPacks.put(summary.getId(), this.wrapSummary(summary));
		}
	}

	@Override
	public void select(Collection<T> packs) {
		this.packRepository.setSelectedPacks(packs.stream().map(T::getId).collect(Collectors.toList()));
	}

	@Override
	public Collection<T> getAvailable() {
		return this.packRepository.getAvailablePacks().stream().map(ResourcePackSummary::getId).map(this.unopenedPacks::get).collect(Collectors.toList());
	}

	@Override
	public Collection<T> getUnselected() {
		return this.packRepository.getUnselectedPacks().stream().map(ResourcePackSummary::getId).map(this.unopenedPacks::get).collect(Collectors.toList());
	}

	@Override
	public Collection<T> getSelected() {
		return this.packRepository.getSelectedPacks().stream().map(ResourcePackSummary::getId).map(this.unopenedPacks::get).collect(Collectors.toList());
	}

	@Override
	public T getPack(String id) {
		return this.unopenedPacks.get(id);
	}

	@Override
	public void addSource(RepositorySource source) {
		this.packRepository.addSource(new WrappedRepositorySource(source));
	}

	@Override
	public void close() {
		this.packRepository.close();
	}

	@SuppressWarnings("unchecked")
	private T wrapSummary(ResourcePackSummary summary) {
		if (summary instanceof WrappedUnopenedPack) {
			return (T) ((WrappedUnopenedPack) summary).unopenedPack;
		} else {
			return UnopenedPack.create(
				summary.getId(),
				summary.isRequired(),
				() -> new ResourcePackAdapter(summary.open()),
				this.factory,
				Adapters.position(summary.getDefaultPosition())
			);
		}
	}

	private class WrappedRepositorySource implements ResourcePackRepository.Source {

		private final RepositorySource source;

		private WrappedRepositorySource(RepositorySource source) {
			this.source = source;
		}

		@Override
		public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
			Map<String, T> packs = new LinkedHashMap<>();
			this.source.loadPacks(packs, ResourcePackRepositoryAdapter.this.factory);

			packs.values().forEach(pack -> {
				consumer.accept(new WrappedUnopenedPack(pack));
			});
		}
	}
}