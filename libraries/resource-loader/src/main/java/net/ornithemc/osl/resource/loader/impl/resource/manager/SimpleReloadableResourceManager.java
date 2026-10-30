package net.ornithemc.osl.resource.loader.impl.resource.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.api.util.Unit;
import net.ornithemc.osl.core.api.util.sort.TopoSort;
import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.manager.ReloadableResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReload;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadContext;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;
import net.ornithemc.osl.resource.loader.api.server.ServerResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.resource.reload.IdentifiableResourceReloader;
import net.ornithemc.osl.resource.loader.impl.resource.reload.NoOpResourceReloader;
import net.ornithemc.osl.resource.loader.impl.resource.reload.ResourceReloaders;
import net.ornithemc.osl.resource.loader.impl.resource.reload.SimpleResourceReloadContext;

public class SimpleReloadableResourceManager implements ReloadableResourceManager {

	private static final Map<ResourceType, SimpleReloadableResourceManager> INSTANCES = new EnumMap<>(ResourceType.class);

	private static SimpleReloadableResourceManager forType(ResourceType type) {
		return INSTANCES.computeIfAbsent(type, SimpleReloadableResourceManager::new);
	}

	public static SimpleReloadableResourceManager client() {
		return forType(ResourceType.CLIENT_ASSETS);
	}

	public static SimpleReloadableResourceManager server() {
		return forType(ResourceType.SERVER_DATA);
	}

	private final ResourceType type;
	private final List<ResourceReloader> registeredReloaders;
	private final List<ResourceReloader> recentlyRegisteredReloaders;
	private final Map<NamespacedIdentifier, ResourceReloader> identifiableReloaders;
	private final Map<NamespacedIdentifier, Set<NamespacedIdentifier>> reloaderDependencies;
	private final List<ResourcePack> resourcePacks;
	private final Map<String, FallbackResourceManager> resourceManagers;
	private final LegacyResourceManager legacyResourceManager;

	private NamespacedIdentifier lastVanillaReloader;

	public SimpleReloadableResourceManager(ResourceType type) {
		this.type = type;
		this.registeredReloaders = new ArrayList<>();
		this.recentlyRegisteredReloaders = new ArrayList<>();
		this.identifiableReloaders = new HashMap<>();
		this.reloaderDependencies = new HashMap<>();
		this.resourcePacks = new ArrayList<>();
		this.resourceManagers = new HashMap<>();
		this.legacyResourceManager = new LegacyResourceManager();
	}

	public void init() {
		if (this.type == ResourceType.CLIENT_ASSETS) {
			ClientResourceLoaderEvents.INIT_RESOURCE_MANAGER.invoker().accept(this);
		} else if (this.type == ResourceType.SERVER_DATA) {
			ServerResourceLoaderEvents.INIT_RESOURCE_MANAGER.invoker().accept(this);
		}

		this.registerReloaderDependencies(ResourceReloaders.AFTER_VANILLA, this.lastVanillaReloader);

		this.lastVanillaReloader = ResourceReloaders.AFTER_VANILLA;
	}

	public void reset() {
		this.registeredReloaders.clear();
		this.recentlyRegisteredReloaders.clear();
		this.identifiableReloaders.clear();
		this.reloaderDependencies.clear();
		this.resourcePacks.clear();
		this.resourceManagers.clear();
		this.legacyResourceManager.clear();

		this.lastVanillaReloader = ResourceReloaders.BEFORE_VANILLA;

		this.registerReloader(ResourceReloaders.BEFORE_VANILLA, NoOpResourceReloader.INSTANCE);
		this.registerReloader(ResourceReloaders.AFTER_VANILLA, NoOpResourceReloader.INSTANCE);
		this.registerReloaderDependencies(ResourceReloaders.AFTER_VANILLA, ResourceReloaders.BEFORE_VANILLA);
	}

	public void addAll(Iterable<ResourcePack> packs) {
		packs.forEach(this::add);
	}

	public void add(ResourcePack pack) {
		pack.flatStream().forEach(this::addResourcePack);
	}

	private void addResourcePack(ResourcePack pack) {
		this.resourcePacks.add(pack);

		for (String namespace : pack.getNamespaces(this.type)) {
			FallbackResourceManager manager = this.resourceManagers.get(namespace);
			if (manager == null) {
				this.resourceManagers.put(namespace, manager = new FallbackResourceManager(this.type, namespace));
			}

			manager.add(pack);
		}

		this.legacyResourceManager.add(pack);
	}

	@Override
	public List<ResourcePack> getResourcePacks() {
		return Collections.unmodifiableList(this.resourcePacks);
	}

	@Override
	public boolean hasResource(String path) {
		return this.legacyResourceManager.hasResource(path);
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		return this.legacyResourceManager.getResource(path);
	}

	@Override
	public List<InputStream> getResourceStack(String path) throws IOException {
		return this.legacyResourceManager.getResourceStack(path);
	}

	@Override
	public Set<String> getNamespaces() {
		return this.resourceManagers.keySet();
	}

	@Override
	public boolean hasResource(NamespacedIdentifier location) {
		ResourceManager manager = this.resourceManagers.get(location.namespace());
		return manager != null && manager.hasResource(location);
	}

	@Override
	public Optional<Resource> getResource(NamespacedIdentifier location) {
		ResourceManager manager = this.resourceManagers.get(location.namespace());
		return manager == null ? Optional.empty() : manager.getResource(location);
	}

	@Override
	public List<Resource> getResourceStack(NamespacedIdentifier location) {
		ResourceManager manager = this.resourceManagers.get(location.namespace());
		return manager == null ? Collections.emptyList() : manager.getResourceStack(location);
	}

	@Override
	public Map<NamespacedIdentifier, Resource> findResources(String directory, Predicate<NamespacedIdentifier> filter) {
		Map<NamespacedIdentifier, Resource> resources = new TreeMap<>(NamespacedIdentifiers.COMPARATOR);

		for (ResourceManager manager : this.resourceManagers.values()) {
			resources.putAll(manager.findResources(directory, filter));
		}

		return resources;
	}

	@Override
	public Map<NamespacedIdentifier, Resource> findResources(String namespace, String directory, Predicate<NamespacedIdentifier> filter) {
		ResourceManager manager = this.resourceManagers.get(namespace);
		return manager == null ? Collections.emptyMap() : manager.findResources(directory, filter);
	}

	@Override
	public Map<NamespacedIdentifier, List<Resource>> findResourceStacks(String directory, Predicate<NamespacedIdentifier> filter) {
		Map<NamespacedIdentifier, List<Resource>> resources = new TreeMap<>(NamespacedIdentifiers.COMPARATOR);

		for (ResourceManager manager : this.resourceManagers.values()) {
			resources.putAll(manager.findResourceStacks(directory, filter));
		}

		return resources;
	}

	@Override
	public Map<NamespacedIdentifier, List<Resource>> findResourceStacks(String namespace, String directory, Predicate<NamespacedIdentifier> filter) {
		ResourceManager manager = this.resourceManagers.get(namespace);
		return manager == null ? Collections.emptyMap() : manager.findResourceStacks(directory, filter);
	}

	@Override
	@Deprecated
	public void addReloader(ResourceReloader reloader) {
		this.registeredReloaders.add(reloader);
		this.recentlyRegisteredReloaders.add(reloader);
	}

	@Deprecated
	public void addReloadedReloader(ResourceReloader reloader) {
		this.registeredReloaders.add(reloader);
	}

	@Override
	public void registerReloader(NamespacedIdentifier identifier, ResourceReloader reloader) {
		this.registerIdentifiableReloader(identifier, reloader, false);
	}

	public void registerReloadedReloader(NamespacedIdentifier identifier, ResourceReloader reloader) {
		this.registerIdentifiableReloader(identifier, reloader, true);
	}

	private void registerIdentifiableReloader(NamespacedIdentifier identifier, ResourceReloader reloader, boolean reloaded) {
		if (this.identifiableReloaders.containsKey(identifier)) {
			throw new IllegalArgumentException("Duplicate resource reloader " + identifier);
		}

		reloader = new IdentifiableResourceReloader(identifier, reloader);

		this.identifiableReloaders.put(identifier, reloader);
		this.registeredReloaders.add(reloader);
		if (!reloaded) {
			this.recentlyRegisteredReloaders.add(reloader);
		}

		if (ResourceReloaders.isVanilla(this.type, identifier)) {
			this.registerReloaderDependencies(identifier, this.lastVanillaReloader);
		}
	}

	@Override
	public void registerReloaderDependencies(NamespacedIdentifier identifier, NamespacedIdentifier... dependencies) {
		this.reloaderDependencies.computeIfAbsent(identifier, key -> new HashSet<>()).addAll(Arrays.asList(dependencies));

		if (ResourceReloaders.isVanilla(this.type, identifier)) {
			this.lastVanillaReloader = identifier;
		}
	}

	@Override
	public void reload(List<ResourcePack> packs) {
		ResourceReload reload = this.startReload(packs, Runnable::run, Runnable::run, CompletableFuture.completedFuture(Unit.INSTANCE));

		if (reload.isDone()) {
			reload.checkExceptions();
		}
	}

	public void partialReload() {
		ResourceReload reload = this.startPartialReload(Runnable::run, Runnable::run, CompletableFuture.completedFuture(Unit.INSTANCE));

		if (reload.isDone()) {
			reload.checkExceptions();
		}
	}

	@Override
	public ResourceReload startReload(List<ResourcePack> packs, Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<?> initialTask) {
		ResourceLoader.LOGGER.info("Reloading ResourceManager: {}", packs.stream().map(ResourcePack::getName).collect(Collectors.joining(", ")));

		this.resourcePacks.clear();
		this.resourceManagers.clear();
		this.legacyResourceManager.clear();

		for (ResourcePack pack : packs) {
			this.add(pack);
		}

		return this.startReload(backgroundExecutor, mainThreadExecutor, initialTask, this.registeredReloaders);
	}

	public ResourceReload startPartialReload(Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<?> initialTask) {
		return this.startReload(backgroundExecutor, mainThreadExecutor, initialTask, this.recentlyRegisteredReloaders);
	}

	private ResourceReload startReload(Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<?> initialTask, List<ResourceReloader> reloaders) {
		if (this.lastVanillaReloader != ResourceReloaders.AFTER_VANILLA) {
			this.registerReloaderDependencies(ResourceReloaders.AFTER_VANILLA, this.lastVanillaReloader);
		}

		reloaders = this.sortReloaders(reloaders);
		this.recentlyRegisteredReloaders.clear();

		ResourceReloadContext context = new SimpleResourceReloadContext(this.resourcePacks, reloaders);

		initialTask = initialTask.thenRun(() -> {
			if (this.type == ResourceType.CLIENT_ASSETS) {
				ClientResourceLoaderEvents.START_RESOURCE_RELOAD.invoker().accept(this, context);
			} else if (this.type == ResourceType.SERVER_DATA) {
				ServerResourceLoaderEvents.START_RESOURCE_RELOAD.invoker().accept(this, context);
			}
		});

		return ResourceReload.start(this, reloaders, backgroundExecutor, mainThreadExecutor, initialTask, () -> {
			if (this.type == ResourceType.CLIENT_ASSETS) {
				ClientResourceLoaderEvents.END_RESOURCE_RELOAD.invoker().accept(this, context);
			} else if (this.type == ResourceType.SERVER_DATA) {
				ServerResourceLoaderEvents.END_RESOURCE_RELOAD.invoker().accept(this, context);
			}
		});
	}

	private List<ResourceReloader> sortReloaders(List<ResourceReloader> reloaders) {
		List<ResourceReloader> unidentifiedReloaders = new ArrayList<>();
		Set<NamespacedIdentifier> reloaderIdentifiers = new HashSet<>();

		reloaderIdentifiers.add(ResourceReloaders.BEFORE_VANILLA);
		reloaderIdentifiers.add(ResourceReloaders.AFTER_VANILLA);

		for (ResourceReloader reloader : reloaders) {
			if (reloader instanceof IdentifiableResourceReloader) {
				IdentifiableResourceReloader identifiableReloader = (IdentifiableResourceReloader) reloader;
				NamespacedIdentifier identifier = identifiableReloader.getIdentifier();

				reloaderIdentifiers.add(identifier);
			} else {
				unidentifiedReloaders.add(reloader);
			}
		}

		List<NamespacedIdentifier> sorted = TopoSort.sort(reloaderIdentifiers, this.reloaderDependencies);

		reloaders = sorted.stream().map(this.identifiableReloaders::get).collect(Collectors.toCollection(ArrayList::new));
		reloaders.addAll(unidentifiedReloaders);

		return reloaders;
	}

	public void singleReload(NamespacedIdentifier identifier) {
		ResourceReloader reloader = this.identifiableReloaders.get(identifier);

		if (reloader == null) {
			throw new IllegalStateException("no such ResourceReloader: " + identifier);
		} else {
			this.singleReload(reloader);
		}
	}

	public void singleReload(ResourceReloader reloader) {
		ResourceReload reload = this.startSingleReload(reloader, Runnable::run, Runnable::run);

		if (reload.isDone()) {
			reload.checkExceptions();
		}
	}

	public ResourceReload startSingleReload(NamespacedIdentifier identifier, Executor backgroundExecutor, Executor mainThreadExecutor) {
		ResourceReloader reloader = this.identifiableReloaders.get(identifier);

		if (reloader == null) {
			throw new IllegalStateException("no such ResourceReloader: " + identifier);
		} else {
			return this.startSingleReload(reloader, backgroundExecutor, mainThreadExecutor);
		}
	}

	public ResourceReload startSingleReload(ResourceReloader reloader, Executor backgroundExecutor, Executor mainThreadExecutor) {
		return ResourceReload.start(this, reloader, backgroundExecutor, mainThreadExecutor);
	}
}
