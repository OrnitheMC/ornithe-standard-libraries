package net.ornithemc.osl.resource.loader.api.resource.reload;

import java.util.List;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;

/**
 * Context for resource reload events.
 */
public interface ResourceReloadContext {

	/**
	 * @return the resource packs selected for the resource reload.
	 */
	List<ResourcePack> resourcePacks();

	/**
	 * @return the resource reloaders selected for the resource reload.
	 */
	List<ResourceReloader> resourceReloaders();

}
