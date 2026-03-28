package net.ornithemc.osl.resource.loader.impl.adapter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.resource.pack.repository.PackRepository;
import net.minecraft.resource.pack.repository.RepositorySource;
import net.minecraft.resource.pack.repository.UnopenedPack;
import net.minecraft.resource.pack.repository.UnopenedPack.Factory;

import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

public class ResourcePackRepositoryAdapter<T extends UnopenedPack> extends PackRepository<T> {

	private final UnopenedPack.Factory<T> factory;
	private final Map<String, T> packs;

	final ResourcePackRepository repository;

	public ResourcePackRepositoryAdapter(Factory<T> factory, ResourcePackRepository repository) {
		super(factory);

		this.factory = factory;
		this.packs = new HashMap<>();

		this.repository = repository;
	}

	private T summaryToPack(ResourcePackSummary summary) {
		return this.packs.get(summary.getId());
	}

	@Override
	public void reload() {
		this.packs.clear();
		this.repository.reload();

		for (ResourcePackSummary summary : this.repository.getAvailablePacks()) {
			this.packs.put(summary.getId(), Adapters.unopenedPack(summary, this.factory));
		}
	}

	@Override
	public void select(Collection<T> packs) {
		this.repository.setSelectedPacks(packs.stream().map(T::getId).collect(Collectors.toList()));
	}

	@Override
	public Collection<T> getAvailable() {
		return this.repository.getAvailablePacks().stream().map(this::summaryToPack).collect(Collectors.toList());
	}

	@Override
	public Collection<T> getUnselected() {
		return this.repository.getUnselectedPacks().stream().map(this::summaryToPack).collect(Collectors.toList());
	}

	@Override
	public Collection<T> getSelected() {
		return this.repository.getSelectedPacks().stream().map(this::summaryToPack).collect(Collectors.toList());
	}

	@Override
	public T getPack(String id) {
		return this.packs.get(id);
	}

	@Override
	public void addSource(RepositorySource source) {
		this.repository.addSource(new WrappedRepositorySource<>(this.factory, source));
	}
}