package net.ornithemc.osl.registries.api;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.registries.api.registry.Registries;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;

/**
 * Events related to the registries lifecycle.
 */
public final class RegistryEvents {

	/**
	 * This event is invoked on game start-up, after Vanilla registries have been
	 * loaded, but before the bootstraps are run and the registries are frozen.
	 * 
	 * <p>
	 * Custom registries should be loaded in listeners to this event. Helper
	 * methods for creating registries can be found in the {@linkplain Registries}
	 * class.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * RegistryEvents.INIT_REGISTRIES.register(() -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 * 
	 * @see Registries
	 * @see SyncedRegistries
	 */
	public static final Event<Runnable> INIT_REGISTRIES = Event.runnable();

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

	public static final Event<Runnable> REGISTRIES_FROZEN = Event.runnable();

}
