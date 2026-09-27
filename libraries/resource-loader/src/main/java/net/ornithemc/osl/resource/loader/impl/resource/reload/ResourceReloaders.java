package net.ornithemc.osl.resource.loader.impl.resource.reload;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;

public final class ResourceReloaders {

	public static final NamespacedIdentifier BEFORE_VANILLA = osl("before_vanilla");
	public static final NamespacedIdentifier AFTER_VANILLA = osl("after_vanilla");

	private static final Map<ResourceType, Set<NamespacedIdentifier>> VANILLA = new EnumMap<>(ResourceType.class);

	public static NamespacedIdentifier vanilla(ResourceType type, String identifier) {
		NamespacedIdentifier id = NamespacedIdentifiers.from(identifier);
		VANILLA.computeIfAbsent(type, key -> new HashSet<>()).add(id);
		return id;
	}

	public static NamespacedIdentifier osl(String identifier) {
		return NamespacedIdentifiers.from("osl", identifier);
	}

	public static boolean isVanilla(ResourceType type, NamespacedIdentifier identifier) {
		return VANILLA.getOrDefault(type, Collections.emptySet()).contains(identifier);
	}
}
