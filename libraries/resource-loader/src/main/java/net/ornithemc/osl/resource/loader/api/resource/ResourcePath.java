package net.ornithemc.osl.resource.loader.api.resource;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

/**
 * Utilities for getting file paths of resources.
 */
public final class ResourcePath {

	/**
	 * Takes the given resource path and returns it as a relative path.
	 * 
	 * @return the resource path.
	 */
	public static String nameOf(String path) {
		if (path.charAt(0) == '/') {
			path = path.substring(1);
		}

		return path;
	}

	/**
	 * @return the resource path for the given resource type and location.
	 */
	public static String nameOf(ResourceType type, NamespacedIdentifier location) {
		return String.format("%s/%s/%s", type.directory(), location.namespace(), location.identifier());
	}

	/**
	 * Resolves the relative resource path from the given path name.
	 * 
	 * @return the relative {@linkplain Path} to the resource.
	 */
	public static Path of(String path) {
		return of(Paths.get("."), path);
	}

	/**
	 * Resolves the relative resource path from the given resource type and location.
	 * 
	 * @return the {relative @linkplain Path} to the resource.
	 */
	public static Path of(ResourceType type, NamespacedIdentifier location) {
		return of(Paths.get("."), type, location);
	}

	/**
	 * Resolves the resource path from the given root and path name.
	 * 
	 * @return the {@linkplain Path} to the resource.
	 */
	public static Path of(Path root, String path) {
		String separator = root.getFileSystem().getSeparator();
		String pathName = nameOf(path).replace("/", separator);

		return root.resolve(pathName).normalize();
	}

	/**
	 * Resolves the resource path from the given root and resource type and location.
	 * 
	 * @return the {@linkplain Path} to the resource.
	 */
	public static Path of(Path root, ResourceType type, NamespacedIdentifier location) {
		String separator = root.getFileSystem().getSeparator();
		String pathName = nameOf(type, location).replace("/", separator);

		return root.resolve(pathName).normalize();
	}
}
