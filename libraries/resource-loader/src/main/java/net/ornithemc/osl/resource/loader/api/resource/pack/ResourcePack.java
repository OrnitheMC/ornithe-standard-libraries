package net.ornithemc.osl.resource.loader.api.resource.pack;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata.Section;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;

public interface ResourcePack extends AutoCloseable {

	/**
	 * The path to the pack.mcmeta file.
	 */
	String METADATA_FILE = "pack" + ResourceMetadata.FILE_EXTENSION;
	/**
	 * The path to the pack.png file.
	 */
	String ICON_FILE = "pack.png";

	/**
	 * @return the ID of this resource pack.
	 */
	String getId();

	/**
	 * @return the name of this resource pack.
	 */
	String getName();

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
	 * Returns the resource at the given path.
	 * <p>
	 * NOTE: this method expects a direct file path to be given,
	 * rather than a namespaced path. For this reason, this method
	 * should only be used to access resources in 1.5.2 and below,
	 * or special root resources such as pack.mcmeta and pack.png.
	 * 
	 * @param path the path to the resource.
	 * @return the resource at the given path.
	 * @throws IOException if the resource could not be found or read.
	 */
	InputStream getResource(String path) throws IOException;

	/**
	 * @return all namespaces available for the given type of resources.
	 */
	Set<String> getNamespaces(ResourceType type);

	/**
	 * Checks whether the resource of the given type at the given
	 * location exists.
	 * 
	 * @param type     the resource type.
	 * @param location the location of the resource.
	 * @return whether the resource exists.
	 */
	boolean hasResource(ResourceType type, NamespacedIdentifier location);

	/**
	 * Returns the resource of the given type at the given location,
	 * or {@code null} if it does not exist.
	 * 
	 * @param location the location of the resource.
	 * @return the top resource at the given location.
	 */
	IOSupplier<InputStream> getResource(ResourceType type, NamespacedIdentifier location);

	/**
	 * Finds all resources that could be found in the given directory
	 * or one of its sub-directories (recursively to the given depth),
	 * that match the given filter.
	 * 
	 * @param type      the resource type.
	 * @param namespace the namespace in which to look for resources.
	 * @param directory the directory in which to start looking for resources.
	 * @param consumer  the consumer of resource locations and corresponding resources.
	 */
	void findResources(ResourceType type, String namespace, String directory, ResourceConsumer consumer);

	/**
	 * Loads, parses, and returns the resource pack metadata section
	 * with the given name, using the given serializer, or {@code null}
	 * if it does not exist.
	 * 
	 * @param <T>          the value type.
	 * @param section      the metadata section type.
	 * @return the parsed resource metadata section, or {@code null}.
	 * @throws IOException if the resource metadata section exists but could not be parsed.
	 */
	default <T> T getMetadata(Section<T> section) throws IOException {
		return this.getMetadata(section.name(), section.serializer());
	}

	/**
	 * Loads, parses, and returns the resource pack metadata section
	 * with the given name, using the given serializer, or {@code null}
	 * if it does not exist.
	 * 
	 * @param <T>          the value type.
	 * @param name         the name of the metadata section.
	 * @param serializer   the serializer of the metadata section.
	 * @return the parsed resource metadata section, or {@code null}.
	 * @throws IOException if the resource metadata section exists but could not be parsed.
	 */
	<T> T getMetadata(String name, Section.Serializer<T> serializer) throws IOException;

	/**
	 * This method may be called when this resource pack is first discovered,
	 * or when it is selected. It is here for legacy reasons, to support old
	 * Minecraft versions where texture packs or resource packs used methods
	 * like this to load metadata and/or icons.
	 */
	void open();

	/**
	 * This method may be called when this resource pack is no longer present,
	 * or when it is unselected. It is here for legacy reasons, to support old
	 * Minecraft versions where texture packs or resource packs used methods
	 * like this to unload metadata and/or icons.
	 */
	@Override
	void close(); // no exception

}
