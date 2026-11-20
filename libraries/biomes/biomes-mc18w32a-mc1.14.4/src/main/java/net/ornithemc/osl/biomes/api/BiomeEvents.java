package net.ornithemc.osl.biomes.api;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the biomes lifecycle.
 */
public final class BiomeEvents {

	/**
	 * This event is invoked upon game start-up, after Vanilla biome registration is finished.
	 * 
	 * <p>
	 * Custom biome registration should be done in a listener of this event.
	 * Helper methods for registering biomes can be found in the {@linkplain
	 * BiomeRegistry} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * BiomeEvents.REGISTER_BIOMES.register(() -> {
	 * 	BiomeRegistry.register(NamespacedIdentifiers.from("example", "cookie"), new CookieBiome());
	 * });
	 * }
	 * </pre>
	 * 
	 * @see BiomeRegistry
	 */
	public static final Event<Runnable> REGISTER_BIOMES = Event.runnable();

}
