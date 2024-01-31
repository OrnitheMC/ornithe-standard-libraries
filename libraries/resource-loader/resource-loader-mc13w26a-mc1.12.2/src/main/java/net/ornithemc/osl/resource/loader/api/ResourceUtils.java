package net.ornithemc.osl.resource.loader.api;

import net.minecraft.resource.Identifier;

public class ResourceUtils {

	public static boolean isValidChar(char chr) {
		return (chr >= '0' && chr <= '9') || (chr >= 'a' && chr <= 'z') || chr == '_' || chr == ':' || chr == '/' || chr == '.' || chr == '-';
	}

	public static boolean isValidIdentifier(Identifier id) {
		return isValidNamespace(id.getNamespace()) && isValidPath(id.getPath());
	}

	public static boolean isValidNamespace(String namespace) {
		return namespace.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || (chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z'));
	}

	public static boolean isValidPath(String namespace) {
		return namespace.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || chr == '/' || (chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z'));
	}
}
