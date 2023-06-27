package net.ornithemc.osl.resource.loader.api;

public class ResourceUtils {

	public static boolean isValidNamespace(String namespace) {
		return namespace.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || (chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z'));
	}

	public static boolean isValidPath(String namespace) {
		return namespace.chars().allMatch(chr -> chr == '-' || chr == '.' || chr == '_' || chr == '/' || (chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z'));
	}
}
