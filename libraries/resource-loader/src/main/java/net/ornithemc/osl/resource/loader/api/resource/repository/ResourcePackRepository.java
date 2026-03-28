package net.ornithemc.osl.resource.loader.api.resource.repository;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

/**
 * A resource pack repository manages resource packs.
 */
public interface ResourcePackRepository extends AutoCloseable {

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
	 * Adds a source from which resource packs are loaded.
	 */
	void addSource(Source source);

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
	default List<ResourcePack> openSelectedPacks() {
		return this.getSelectedPacks().stream().map(ResourcePackSummary::open).collect(Collectors.toList());
	}

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
