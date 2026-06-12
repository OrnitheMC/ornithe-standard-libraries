package net.ornithemc.osl.resource.loader.api.resource.manager;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
	 * @param reloader
	 */
	void addReloader(ResourceReloader reloader);

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
