package net.ornithemc.osl.resource.loader.api.server.resource.reload;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.impl.resource.reload.ResourceReloaders;

public final class DataReloaders {

	public static final NamespacedIdentifier TAG_MANAGER = vanilla("tags");
	public static final NamespacedIdentifier CRAFTING_MANAGER = vanilla("recipes");
	public static final NamespacedIdentifier LOOT_TABLE_MANAGER = vanilla("loot_tables");
	public static final NamespacedIdentifier FUNCTION_MANAGER = vanilla("functions");
	public static final NamespacedIdentifier ADVANCEMENT_MANAGER = vanilla("advancements");
	public static final NamespacedIdentifier STRUCTURE_MANAGER = vanilla("structures");

	public static final NamespacedIdentifier BEFORE_VANILLA = ResourceReloaders.BEFORE_VANILLA;
	public static final NamespacedIdentifier AFTER_VANILLA = ResourceReloaders.AFTER_VANILLA;

	private static NamespacedIdentifier vanilla(String identifier) {
		return ResourceReloaders.vanilla(ResourceType.SERVER_DATA, identifier);
	}
}
