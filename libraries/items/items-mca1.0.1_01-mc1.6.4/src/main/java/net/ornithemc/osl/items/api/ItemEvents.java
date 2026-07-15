package net.ornithemc.osl.items.api;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the items lifecycle.
 */
public final class ItemEvents {

	/**
	 * This event is invoked upon game start-up, after Vanilla item registration is finished.
	 * 
	 * <p>
	 * Custom item registration should be done in a listener of this event.
	 * Helper methods for registering items can be found in the {@linkplain
	 * ItemRegistry} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ItemEvents.REGISTER_ITEMS.register(() -> {
	 * 	ItemRegistry.register(NamespacedIdentifiers.from("example", "cookie"), new CookieItem(AUTO_ASSIGN_ID));
	 * });
	 * }
	 * </pre>
	 * 
	 * @see ItemRegistry
	 */
	public static final Event<Runnable> REGISTER_ITEMS = Event.runnable();

	/**
	 * This event is invoked upon game start-up, after Vanilla block item registration is finished.
	 * 
	 * <p>
	 * Custom block item registration should be done in a listener of this event.
	 * Helper methods for registering block items can be found in the {@linkplain
	 * ItemRegistry} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ItemEvents.REGISTER_BLOCK_ITEMS.register(() -> {
	 * 	ItemRegistry.register(COOKIE_BLOCK, new CookieItem(AUTO_ASSIGN_ID));
	 * });
	 * }
	 * </pre>
	 * 
	 * @see ItemRegistry
	 */
	public static final Event<Runnable> REGISTER_BLOCK_ITEMS = Event.runnable();

}
