package net.ornithemc.osl.resource.loader.api.resource;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

/**
 * Utilities for validating resource locations.
 */
public final class ResourceLocation {

	private static final boolean UPPERCASE_ALLOWED = ResourcePacks.getSupportedFormat() < 3;

	/**
	 * @return whether the given {@linkplain String} is a valid resource location.
	 */
	public static boolean isValid(NamespacedIdentifier resourceLocation) {
		return isValid(resourceLocation.toString());
	}

	/**
	 * @return whether the given {@linkplain String} is valid in a resource location.
	 */
	public static boolean isValid(String s) {
		return s.chars().allMatch(ResourceLocation::isCharAllowed);
	}

	/**
	 * @return whether the given character is allowed in resource locations.
	 */
	public static boolean isCharAllowed(int chr) {
		return chr == ':' || chr == '-' || chr == '.' || chr == '_' || chr == '/' || (chr >= 'a' && chr <= 'z') || (UPPERCASE_ALLOWED && chr >= 'A' && chr <= 'Z') || (chr >= '0' && chr <= '9');
	}
}
