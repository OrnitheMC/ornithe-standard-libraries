package net.ornithemc.osl.resource.loader.api.client.resource.reload;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.impl.resource.reload.ResourceReloaders;

public final class AssetsReloaders {

	public static final NamespacedIdentifier LANGUAGE_MANAGER = vanilla("languages");
	public static final NamespacedIdentifier TEXTURE_MANAGER = vanilla("textures");
	public static final NamespacedIdentifier SOUND_MANAGER = vanilla("sounds");
	public static final NamespacedIdentifier FONT_MANAGER = vanilla("fonts");
	public static final NamespacedIdentifier ENCHANTING_PHRASE_FONT_MANAGER = vanilla("enchanting_phrase_fonts");
	public static final NamespacedIdentifier GRASS_COLORS = vanilla("grass_colors");
	public static final NamespacedIdentifier FOLIAGE_COLORS = vanilla("foliage_colors");
	public static final NamespacedIdentifier ITEM_RENDERER = vanilla("item_renderer");
	public static final NamespacedIdentifier GAME_RENDERER = vanilla("game_renderer");
	public static final NamespacedIdentifier BLOCK_RENDERER = vanilla("block_renderer");
	public static final NamespacedIdentifier WORLD_RENDERER = vanilla("level_renderer");

	public static final NamespacedIdentifier BEFORE_VANILLA = ResourceReloaders.BEFORE_VANILLA;
	public static final NamespacedIdentifier AFTER_VANILLA = ResourceReloaders.AFTER_VANILLA;

	private static NamespacedIdentifier vanilla(String identifier) {
		return ResourceReloaders.vanilla(ResourceType.CLIENT_ASSETS, identifier);
	}
}
