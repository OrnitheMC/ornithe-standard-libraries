package net.ornithemc.osl.resource.loader.api.resource.manager;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReload;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloader;

/**
 * A resource manager that can be reloaded.
 */
public interface ReloadableResourceManager extends ResourceManager {

	/**
	 * Adds a reloader to this resource manager that will be triggered on every resource reload.
	 * 
	 * @param reloader the resource reloader to add.
	 * 
	 * @deprecated use {@linkplain #registerReloader(NamespacedIdentifier, ResourceReloader)} instead.
	 */
	@Deprecated
	void addReloader(ResourceReloader reloader);

	/**
	 * Registers a reloader to this resource manager that will be triggered on every resource reload.
	 * 
	 * @param identifier the namespaced ID of the reloader to be registered.
	 * @param reloader   the resource reloader to be registered.
	 */
	void registerReloader(NamespacedIdentifier identifier, ResourceReloader reloader);

	/**
	 * Registers dependencies for a resource reloader, i.e. ensure a resource reloader will run after
	 * the other specified resource reloaders. Any resource reloaders without dependencies will run
	 * last, with no defined ordering between them. All Vanilla reloaders are given identifiers, and
	 * OSL provides markers for running reloaders before or after all Vanilla reloaders.
	 * 
	 * @param identifier   identifier of the resource reloader for which to register the dependencies.
	 * @param dependencies identifiers of the dependencies: the resource reloaders that must run first.
	 * 
	 * @see net.ornithemc.osl.resource.loader.api.client.AssetsReloaders
	 * @see net.ornithemc.osl.resource.loader.api.server.DataReloaders
	 */
	void registerReloaderDependencies(NamespacedIdentifier identifier, NamespacedIdentifier... dependencies);

	/**
	 * Reload all resources, blocking the current thread until finished.
	 */
	void reload(List<ResourcePack> packs);

	/**
	 * Initiate a full resource reload, loading resources on the background thread
	 * and then applying the newly loaded resources on the main thread executor.
	 */
	ResourceReload startReload(List<ResourcePack> packs, Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<?> initialTask);

}
