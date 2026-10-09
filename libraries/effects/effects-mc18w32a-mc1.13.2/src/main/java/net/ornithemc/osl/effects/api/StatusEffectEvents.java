package net.ornithemc.osl.effects.api;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the status effects lifecycle.
 */
public final class StatusEffectEvents {

	/**
	 * This event is invoked upon game start-up, after Vanilla effect registration is finished.
	 * 
	 * <p>
	 * Custom effect registration should be done in a listener of this event.
	 * Helper methods for registering effects can be found in the {@linkplain
	 * StatusEffectRegistry} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * StatusEffectEvents.REGISTER_STATUS_EFFECTS.register(() -> {
	 * 	StatusEffectRegistry.register(NamespacedIdentifiers.from("example", "cookie"), new CookieStatusEffect());
	 * });
	 * }
	 * </pre>
	 * 
	 * @see StatusEffectRegistry
	 */
	public static final Event<Runnable> REGISTER_STATUS_EFFECTS = Event.runnable();

}
