package net.ornithemc.osl.resource.loader.api.resource.repository;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

/**
 * A resource pack repository manages resource packs.
 */
public interface ResourcePackRepository extends AutoCloseable {

	/**
	 * @return the pack repository for the Minecraft client.
	 */
	static ResourcePackRepository client() {
		return SimpleResourcePackRepository.client();
	}

	/**
	 * @return the pack repository for the Minecraft server.
	 */
	static ResourcePackRepository server() {
		return SimpleResourcePackRepository.server();
	}

	/**
	 * @return the pack source for resource packs provided by the Minecraft client.
	 */
	static ClientPackSource clientPackSource() {
		return SimpleResourcePackRepository.clientPackSource();
	}

	/**
	 * @return the pack source for resource packs provided by the Minecraft server.
	 */
	static ServerPackSource serverPackSource() {
		return SimpleResourcePackRepository.serverPackSource();
	}

	/**
	 * Registers a custom bundled resource pack for the given mod,
	 * located at the given directory in the mod's resources.
	 * 
	 * @param id        the ID of the resource pack.
	 * @param name      the display name of the resource pack.
	 * @param mod       the container of the mod that has the resources.
	 * @param directory the directory at which the resources are located.
	 */
	static void registerBundledModResourcePack(String id, String name, ModContainer mod, String directory) {
		BundledModResourcePacks.registerBundledModResourcePack(id, name, mod, directory);
	}

	/**
	 * Adds a source from which resource packs are loaded, with the
	 * preferred ordering {@linkplain PackSourcePosition#LAST}. See
	 * {@linkplain #addSource(Source, PackSourcePosition)} for more
	 * details on pack source ordering.
	 * 
	 * @see #addSource(Source, PackSourcePosition)
	 */
	void addSource(Source source);

	/**
	 * Adds a source from which resource packs are loaded, with the
	 * given preferred ordering. Any pack source added through
	 * {@linkplain #addSource(Source)} will have preferred ordering
	 * {@linkplain PackSourcePosition#LAST}.
	 * <P>
	 * Pack sources at position {@linkplain PackSourcePosition#FIRST}
	 * will be sorted before pack sources at position {@linkplain
	 * PackSourcePosition#LAST}. All pack sources at the same position
	 * will be ordered FIFO (i.e. in the order they were added.
	 * <p>
	 * Vanilla's pack source has preferred ordering {@linkplain
	 * PackSourcePosition#FIRST} while OSL's bundled mod resources have
	 * preferred ordering {@linkplain PackSourcePosition#LAST}.
	 * <p>
	 * The order of pack sources determines the order in which resource
	 * packs are listed. While this order is configurable by the user,
	 * it can be useful to have some control over the <i>default</i>
	 * ordering, for example for mods that add Vanilla-like content
	 * that other mods are expected to override. In such a case, a pack
	 * source can be added with preferred ordering {@linkplain
	 * PackSourcePosition#FIRST} to ensure it is sorted before OSL's
	 * bundled mod resources.
	 */
	void addSource(Source source, PackSourcePosition position);

	/**
	 * Reloads this resource pack repository. All sources will be
	 * reloaded again, and the collection of available resource
	 * packs reconstructed.
	 * <p>
	 * If any of the selected resource packs are no longer available
	 * after reloading, they will be closed and unselected.
	 */
	void reload();

	/**
	 * @return the summaries of all available resource packs.
	 */
	Collection<ResourcePackSummary> getAvailablePacks();

	/**
	 * @return the summaries of all selected resource packs.
	 */
	Collection<ResourcePackSummary> getSelectedPacks();

	/**
	 * @return all selected resource packs.
	 */
	List<ResourcePack> openSelectedPacks();

	/**
	 * Updates the selected resource packs, closing any that are unselected.
	 * 
	 * @param packs the IDs of the selected resource packs.
	 */
	void setSelectedPacks(Collection<String> packs);

	/**
	 * @return the summaries of all available resource packs that are not selected.
	 */
	Collection<ResourcePackSummary> getUnselectedPacks();

	/**
	 * @param id the ID of the resource pack.
	 * @return the summary of the resource pack with the given ID, or {@code null}.
	 */
	ResourcePackSummary getPack(String id);

	@Override
	void close(); // no exception

	/**
	 * Represents a source from which resource packs are loaded.
	 */
	interface Source {

		/**
		 * Loads resource packs from this source and passes them to the given consumer.
		 */
		void loadResourcePacks(Consumer<ResourcePackSummary> consumer);

	}
}
