package net.ornithemc.osl.resource.loader.impl.resource.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ClientPackSource;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.api.resource.repository.ServerPackSource;
import net.ornithemc.osl.resource.loader.api.server.ServerResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

public class SimpleResourcePackRepository implements ResourcePackRepository {

	private static final Map<ResourceType, SimpleResourcePackRepository> INSTANCES = new EnumMap<>(ResourceType.class);

	static ClientPackSource clientPackSource;
	static ServerPackSource serverPackSource;

	private static SimpleResourcePackRepository forType(ResourceType type) {
		return INSTANCES.computeIfAbsent(type, SimpleResourcePackRepository::new);
	}

	public static SimpleResourcePackRepository client() {
		return forType(ResourceType.CLIENT_ASSETS);
	}

	public static SimpleResourcePackRepository server() {
		return forType(ResourceType.SERVER_DATA);
	}

	public static ClientPackSource clientPackSource() {
		return clientPackSource;
	}

	public static ServerPackSource serverPackSource() {
		return serverPackSource;
	}

	private final ResourceType type;
	private final Set<Source> sources;
	private final Map<String, ResourcePackSummary> packs;
	private final List<ResourcePackSummary> available;
	private final List<ResourcePackSummary> selected;

	// these callbacks are used to sync vanilla pack lists
	private Runnable discoveryCallback;
	private Runnable selectionCallback;

	public SimpleResourcePackRepository(ResourceType type) {
		this.type = type;
		this.sources = new LinkedHashSet<>();
		this.packs = new LinkedHashMap<>();
		this.available = new ArrayList<>();
		this.selected = new ArrayList<>();
	}

	public void setCallbacks(Runnable discovery, Runnable selection) {
		this.discoveryCallback = discovery;
		this.selectionCallback = selection;
	}

	public void init() {
		if (this.type == ResourceType.CLIENT_ASSETS) {
			ClientResourceLoaderEvents.INIT_RESOURCE_PACK_REPOSITORY.invoker().accept(this);
		} else if (this.type == ResourceType.SERVER_DATA) {
			ServerResourceLoaderEvents.INIT_RESOURCE_PACK_REPOSITORY.invoker().accept(this);
		}
	}

	public void reset() {
		this.close();

		this.sources.clear();
		this.packs.clear();
		this.available.clear();
		this.selected.clear();

		this.discoveryCallback = null;
		this.selectionCallback = null;
	}

	@Override
	public void addSource(Source source) {
		this.sources.add(source);
	}

	@Override
	public void reload() {
		this.close();

		if (this.type == ResourceType.CLIENT_ASSETS) {
			ClientResourceLoaderEvents.START_RESOURCE_PACKS_RELOAD.invoker().accept(this);
		} else if (this.type == ResourceType.SERVER_DATA) {
			ServerResourceLoaderEvents.START_RESOURCE_PACKS_RELOAD.invoker().accept(this);
		}

		List<String> selection = this.selected.stream().map(ResourcePackSummary::getId).collect(Collectors.toList());

		this.discoverAvailablePacks();
		this.rebuildSelectedPacks(selection);

		if (this.type == ResourceType.CLIENT_ASSETS) {
			ClientResourceLoaderEvents.END_RESOURCE_PACKS_RELOAD.invoker().accept(this);
		} else if (this.type == ResourceType.SERVER_DATA) {
			ServerResourceLoaderEvents.END_RESOURCE_PACKS_RELOAD.invoker().accept(this);
		}
	}

	private void discoverAvailablePacks() {
		this.packs.clear();
		this.available.clear();

		for (Source source : this.sources) {
			source.loadResourcePacks(pack -> {
				if (this.packs.containsKey(pack.getId())) {
					ResourceLoader.LOGGER.info("Duplicate ResourcePack '{}'", pack.getId());
				} else {
					this.packs.put(pack.getId(), pack);
					this.available.add(0, pack);
				}
			});
		}

		if (this.discoveryCallback != null) {
			this.discoveryCallback.run();
		}
	}

	private void rebuildSelectedPacks(Collection<String> selection) {
		this.selected.clear();
		this.selected.addAll(this.getAvailablePacks(selection));

		for (ResourcePackSummary pack : this.available) {
			if (pack.isRequired() && !this.selected.contains(pack)) {
				pack.getDefaultPosition().insert(this.selected, pack, false);
			}
		}

		this.selected.stream().map(ResourcePackSummary::open).forEach(ResourcePack::open);

		if (this.selectionCallback != null) {
			this.selectionCallback.run();
		}
	}

	@Override
	public Collection<ResourcePackSummary> getAvailablePacks() {
		return this.available;
	}

	private List<ResourcePackSummary> getAvailablePacks(Collection<String> selection) {
		return selection.stream().map(this.packs::get).filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Override
	public Collection<ResourcePackSummary> getSelectedPacks() {
		return this.selected;
	}

	@Override
	public void setSelectedPacks(Collection<String> packs) {
		this.rebuildSelectedPacks(packs);
	}

	@Override
	public Collection<ResourcePackSummary> getUnselectedPacks() {
		return this.available.stream().filter(p -> !this.selected.contains(p)).collect(Collectors.toList());
	}

	@Override
	public ResourcePackSummary getPack(String id) {
		return this.packs.get(id);
	}

	@Override
	public void close() {
		this.available.forEach(ResourcePackSummary::close);
	}
}
