package net.ornithemc.osl.lifecycle.api;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the registry bootstrap on game start-up.
 */
public final class LifecycleEvents {

	/**
	 * This event is fired on game start-up, before registries are bootstrapped.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * LifecycleEvents.BOOTSTRAP_START.register(() -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Runnable> BOOTSTRAP_START = Event.runnable();
	/**
	 * This event is fired on game start-up, after registries are bootstrapped.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * LifecycleEvents.BOOTSTRAP_END.register(() -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Runnable> BOOTSTRAP_END   = Event.runnable();

}
