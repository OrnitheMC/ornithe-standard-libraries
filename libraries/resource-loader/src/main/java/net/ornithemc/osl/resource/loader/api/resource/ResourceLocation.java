package net.ornithemc.osl.resource.loader.api.resource;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierException;
import net.ornithemc.osl.core.impl.util.NamespacedIdentifierImpl;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

/**
 * Utilities for validating resource locations.
 */
public final class ResourceLocation {

	private static final boolean NO_UPPER_CASE = ResourcePacks.getSupportedFormat() > 2;
	private static final boolean STRICT_VALIDATION = ResourcePacks.getSupportedFormat() > 3;

	/**
	 * Construct a resource location from the given namespace and path.
	 * 
	 * @return a resource location with the given namespace and path.
	 * @throws NamespacedIdentifierException
	 * 	 if the resource location contains illegal characters.
	 */
	public static NamespacedIdentifier of(String namespace, String path) {
		if (!isValidNamespace(namespace)) {
			throw NamespacedIdentifierException.invalidNamespace(namespace, "namespace contains illegal characters");
		} else if (!isValidPath(path)) {
			throw NamespacedIdentifierException.invalidIdentifier(path, "path contains illegal characters");
		} else {
			return new NamespacedIdentifierImpl(namespace, path);
		}
	}

	/**
	 * @return whether the given {@linkplain NamespacedIdentifier} is a valid resource location.
	 */
	public static boolean isValid(NamespacedIdentifier location) {
		return isValidNamespace(location.namespace()) && isValidPath(location.identifier());
	}

	/**
	 * @return whether the given {@linkplain String} is a valid resource namespace.
	 */
	public static boolean isValidNamespace(String namespace) {
		return STRICT_VALIDATION
			? namespace.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || (chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9'))
			: namespace.chars().noneMatch(Character::isUpperCase);
	}

	/**
	 * @return whether the given {@linkplain String} is a valid resource path.
	 */
	public static boolean isValidPath(String path) {
		return STRICT_VALIDATION
			? path.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || chr == '/' || (chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9'))
			: (!NO_UPPER_CASE || path.chars().noneMatch(Character::isUpperCase));
	}

	/**
	 * @return whether the given character is allowed in a resource location.
	 */
	public static boolean isCharAllowed(int chr) {
		return STRICT_VALIDATION
			? (chr == ':' || chr == '-' || chr == '.' || chr == '_' || chr == '/' || (chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9'))
			: (!NO_UPPER_CASE || !Character.isUpperCase(chr));
	}
}
