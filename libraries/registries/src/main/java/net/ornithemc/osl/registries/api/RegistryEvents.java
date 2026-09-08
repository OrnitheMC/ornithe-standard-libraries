package net.ornithemc.osl.registries.api;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to registries.
 */
public final class RegistryEvents {

	/**
	 * This event is invoked on game start-up, after registries have been loaded,
	 * but before the bootstraps are run and the registries are frozen.
	 * Note that bootstraps may run before this point, if they are triggered by
	 * class loading. If you wish to hook into a particular registry's bootstrap,
	 * a Mixin injector is required.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * RegistryEvents.BOOTSTRAP_REGISTRIES.register(() -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Runnable> BOOTSTRAP_REGISTRIES = Event.runnable();

}
