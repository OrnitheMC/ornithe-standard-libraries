package net.ornithemc.osl.resource.loader.api.resource.repository;

import java.util.function.Supplier;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackCompatibility;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackSummary;
import net.ornithemc.osl.text.api.TextComponent;

/**
 * A summary of a resource pack.
 */
public interface ResourcePackSummary extends AutoCloseable {

	static ResourcePackSummary create(ResourcePack pack, String id, boolean required, boolean fixedPosition, PackPosition defaultPosition) {
		return SimpleResourcePackSummary.create(pack, id, required, fixedPosition, defaultPosition);
	}

	static ResourcePackSummary create(String id, boolean required, boolean fixedPosition, PackPosition defaultPosition, Supplier<ResourcePack> opener) {
		return SimpleResourcePackSummary.create(id, required, fixedPosition, defaultPosition, opener);
	}

	/**
	 * @return the display title of the resource pack.
	 */
	TextComponent getTitle();

	/**
	 * @return the display description of the resource pack.
	 */
	TextComponent getDescription();

	/**
	 * @return the ID of the resource pack.
	 */
	String getId();

	/**
	 * @return whether the resource pack is required and thus always selected.
	 */
	boolean isRequired();

	/**
	 * @return whether the position of the resource pack in the ordering is fixed.
	 */
	boolean isFixedPosition();

	/**
	 * @return the default position of the resource pack in the ordering.
	 */
	PackPosition getDefaultPosition();

	/**
	 * @return the compatibility status of the resource pack.
	 */
	PackCompatibility getCompatibility();

	/**
	 * @return the resource pack.
	 */
	ResourcePack open();

	@Override
	void close(); // no exception

}
