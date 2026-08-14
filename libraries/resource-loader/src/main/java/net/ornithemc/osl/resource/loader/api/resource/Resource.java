package net.ornithemc.osl.resource.loader.api.resource;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.impl.resource.LazyResource;

/**
 * A game resource.
 */
public interface Resource extends Closeable {

	/**
	 * @return a resource supplier for the file at the given path.
	 */
	static IOSupplier<InputStream> supplier(Path path) {
		return LazyResource.inputStreamSupplier(path);
	}

	/**
	 * Gives the name of the source where this resource was found,
	 * most likely it is the id of a resource pack.
	 * 
	 * @return the name of the source where this resource was found.
	 */
	String sourceName();

	/**
	 * Gives the location of this resource. The location is given as
	 * a namespaced path. The path is not resolved directly from the
	 * root of resource packs, but from the namespaced directory for
	 * the given resource type.
	 * 
	 * @return the location of this resource.
	 * @see ResourcePath
	 */
	NamespacedIdentifier location();

	/**
	 * @return this resource as an {@linkplain InputStream}.
	 * @throws IOException if an error occurs when opening this resource.
	 */
	InputStream open() throws IOException;

	/**
	 * Opens this resource as a UTF-8 text file.
	 * 
	 * @return this resource as a {@linkplain BufferedReader}.
	 * @throws IOException if an error occurs when opening this resource.
	 */
	BufferedReader openAsReader() throws IOException;

	/**
	 * @return the metadata of this resource.
	 * @throws IOException if an error occurs when opening the metadata.
	 */
	ResourceMetadata metadata() throws IOException;

	/**
	 * @return whether this resource has metadata.
	 */
	boolean hasMetadata();

}
