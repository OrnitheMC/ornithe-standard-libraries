package net.ornithemc.osl.blocks.api;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the blocks lifecycle.
 */
public final class BlockEvents {

	/**
	 * This event is invoked upon game start-up, after Vanilla block registration is finished.
	 * 
	 * <p>
	 * Custom block registration should be done in a listener of this event.
	 * Helper methods for registering blocks can be found in the {@linkplain
	 * BlockRegistry} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * BlockEvents.REGISTER_BLOCKS.register(() -> {
	 * 	BlockRegistry.register(NamespacedIdentifiers.from("example", "cookie"), new CookieBlock(AUTO_ASSIGN_ID));
	 * });
	 * }
	 * </pre>
	 * 
	 * @see BlockRegistry
	 */
	public static final Event<Runnable> REGISTER_BLOCKS = Event.runnable();

}
