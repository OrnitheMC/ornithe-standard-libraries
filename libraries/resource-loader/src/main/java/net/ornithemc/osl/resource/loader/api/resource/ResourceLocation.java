package net.ornithemc.osl.resource.loader.api.resource;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

/**
 * Utilities for validating resource locations.
 */
public final class ResourceLocation {

	private static final boolean NO_UPPER_CASE = ResourcePacks.getSupportedFormat() > 2;
	private static final boolean STRICT_VALIDATION = ResourcePacks.getSupportedFormat() > 3;

	/**
	 * @return whether the given {@linkplain String} is a valid resource location.
	 */
	public static boolean isValid(NamespacedIdentifier resourceLocation) {
		return isValidNamespace(resourceLocation.namespace()) && isValidPath(resourceLocation.identifier());
	}

	/**
	 * @return whether the given {@linkplain String} is a valid resource location namespace.
	 */
	public static boolean isValidNamespace(String namespace) {
		return STRICT_VALIDATION
			? namespace.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || (chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9'))
			: namespace.chars().noneMatch(Character::isUpperCase);
	}

	/**
	 * @return whether the given {@linkplain String} is a valid resource location path.
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
