package net.ornithemc.osl.resource.loader.api.resource;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;

/**
 * Utilities for getting file paths of resources.
 */
public final class ResourcePath {

	/**
	 * @return the given path name as a relative file path.
	 */
	public static String relative(String pathName) {
		if (pathName.charAt(0) == '/') {
			pathName = pathName.substring(1);
		}

		return pathName;
	}

	/**
	 * @return the given path name as an absolute file path.
	 */
	public static String absolute(String pathName) {
		if (pathName.charAt(0) != '/') {
			pathName = "/" + pathName;
		}

		return pathName;
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
		String pathName = relative(path).replace("/", separator);

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
