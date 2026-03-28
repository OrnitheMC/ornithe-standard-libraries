package net.ornithemc.osl.resource.loader.api.resource.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;

/**
 * The resource manager is the access layer for the game's resources.
 */
public interface ResourceManager {

	/**
	 * @return the resource manager for the Minecraft client.
	 */
	static ReloadableResourceManager client() {
		return SimpleReloadableResourceManager.client();
	}

	/**
	 * @return the resource manager for the Minecraft server.
	 */
	static ReloadableResourceManager server() {
		return SimpleReloadableResourceManager.server();
	}

	/**
	 * Checks whether the resource at the given path exists.
	 * <p>
	 * NOTE: this method expects a direct file path to be given,
	 * rather than a namespaced path. For this reason, this method
	 * should only be used to access resources in 1.5.2 and below,
	 * or special root resources such as pack.mcmeta and pack.png.
	 * 
	 * @param path the path to the resource.
	 * @return whether the resource exists.
	 */
	boolean hasResource(String path);

	/**
	 * Returns the top resource at the given path.
	 * <p>
	 * If multiple resource packs contain the resource at the given
	 * path, the resource from the top-most resource pack is picked.
	 * <p>
	 * NOTE: this method expects a direct file path to be given,
	 * rather than a namespaced path. For this reason, this method
	 * should only be used to access resources in 1.5.2 and below,
	 * or special root resources such as pack.mcmeta and pack.png.
	 * 
	 * @param path the path to the resource.
	 * @return the top resource at the given path.
	 * @throws IOException if the resource could not be found or read.
	 */
	InputStream getResource(String path) throws IOException;

	/**
	 * Returns all resources at the given path.
	 * <p>
	 * If multiple resource packs contain the resource at the given
	 * path, the resources are listed in resource pack order. This
	 * means the resource from the top resource pack appears at the
	 * top of the stack (i.e. last in the list).
	 * <p>
	 * NOTE: this method expects a direct file path to be given,
	 * rather than a namespaced path. For this reason, this method
	 * should only be used to access resources in 1.5.2 and below,
	 * or special root resources such as pack.mcmeta and pack.png.
	 * 
	 * @param path the path to the resources.
	 * @return the resources at the given path.
	 * @throws IOException if the resources could not be found or read.
	 */
	List<InputStream> getResourceStack(String path) throws IOException;

	/**
	 * @return all namespaces available from the loaded resource packs.
	 */
	Set<String> getNamespaces();

	/**
	 * Checks whether the resource at the given location exists.
	 * 
	 * @param location the location of the resource.
	 * @return whether the resource exists.
	 */
	boolean hasResource(NamespacedIdentifier location);

	/**
	 * Returns the top resource at the given location.
	 * <p>
	 * If multiple resource packs contain the resource at the given
	 * location, the resource from the top-most resource pack is picked.
	 * <p>
	 * The resource is returned as an {@linkplain Optional} and may
	 * be empty if the resource could not be found.
	 * 
	 * @param location the location of the resource.
	 * @return the top resource at the given location.
	 */
	Optional<Resource> getResource(NamespacedIdentifier location);

	/**
	 * Returns all resources at the given location.
	 * <p>
	 * If multiple resource packs contain the resource at the given
	 * location, the resources are listed in resource pack order. This
	 * means the resource from the top resource pack appears at the
	 * top of the stack (i.e. last in the list).
	 * 
	 * @param location the location of the resources.
	 * @return the resources at the given location.
	 */
	List<Resource> getResourceStack(NamespacedIdentifier location);

	/**
	 * Gives a map of all resources that could be found in the given
	 * directory or one of its sub-directories (recursively), that
	 * match the given filter.
	 * <p>
	 * For each location, only the top resource is given. If multiple
	 * resource packs contain the resource at a given location, the
	 * resource from the top-most resource pack is picked.
	 * 
	 * @param directory the directory in which to start looking for resources.
	 * @param filter    the filter that resource locations must match.
	 * @return a map of found resource locations to corresponding resources.
	 */
	Map<NamespacedIdentifier, Resource> findResources(String directory, Predicate<NamespacedIdentifier> filter);

	/**
	 * Gives a map of all resources that could be found in the given
	 * directory or one of its sub-directories (recursively), in the
	 * given namespace, that match the given filter.
	 * <p>
	 * For each location, only the top resource is given. If multiple
	 * resource packs contain the resource at a given location, the
	 * resource from the top-most resource pack is picked.
	 * 
	 * @param namespace the namespace in which to look for resources.
	 * @param directory the directory in which to start looking for resources.
	 * @param filter    the filter that resource locations must match.
	 * @return a map of found resource locations to corresponding resources.
	 */
	Map<NamespacedIdentifier, Resource> findResources(String namespace, String directory, Predicate<NamespacedIdentifier> filter);

	/**
	 * Gives a map of all resource stacks that could be found in the
	 * given directory or one of its sub-directories (recursively),
	 * that match the given filter.
	 * <p>
	 * For each location, all resources at that location are given. If
	 * multiple resource packs contain the resource at that location,
	 * the resources are listed in resource pack order. This means the
	 * resource from the top resource pack appears at the top of the
	 * stack (i.e. last in the list).
	 * 
	 * @param directory the directory in which to start looking for resources.
	 * @param filter    the filter that resource locations must match.
	 * @return a map of found resource locations to corresponding resource stacks.
	 */
	Map<NamespacedIdentifier, List<Resource>> findResourceStacks(String directory, Predicate<NamespacedIdentifier> filter);

	/**
	 * Gives a map of all resource stacks that could be found in the
	 * given directory or one of its sub-directories (recursively),
	 * in the given namespace, that match the given filter.
	 * <p>
	 * For each location, all resources at that location are given. If
	 * multiple resource packs contain the resource at that location,
	 * the resources are listed in resource pack order. This means the
	 * resource from the top resource pack appears at the top of the
	 * stack (i.e. last in the list).
	 * 
	 * @param namespace the namespace in which to look for resources.
	 * @param directory the directory in which to start looking for resources.
	 * @param filter    the filter that resource locations must match.
	 * @return a map of found resource locations to corresponding resource stacks.
	 */
	Map<NamespacedIdentifier, List<Resource>> findResourceStacks(String namespace, String directory, Predicate<NamespacedIdentifier> filter);

}
