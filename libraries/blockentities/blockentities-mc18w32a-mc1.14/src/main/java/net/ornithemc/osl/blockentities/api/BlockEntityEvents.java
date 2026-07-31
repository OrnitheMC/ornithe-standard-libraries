package net.ornithemc.osl.blockentities.api;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the block entities lifecycle.
 */
public final class BlockEntityEvents {

	/**
	 * This event is invoked upon game start-up, after Vanilla block entity registration is finished.
	 * 
	 * <p>
	 * Custom block entity type registration should be done in a listener of this event.
	 * Helper methods for registering block entity types can be found in the {@linkplain
	 * BlockEntityTypeRegistry} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * BlockEntityEvents.REGISTER_BLOCK_ENTITY_TYPES.register(() -> {
	 * 	BlockEntityTypeRegistry.register(NamespacedIdentifiers.from("example", "cookie"), BlockEntityTypes.builder(CookieBlockEntity::new));
	 * });
	 * }
	 * </pre>
	 * 
	 * @see BlockEntityTypeRegistry
	 * @see BlockEntityTypes
	 */
	public static final Event<Runnable> REGISTER_BLOCK_ENTITY_TYPES = Event.runnable();

}
