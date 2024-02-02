package net.ornithemc.osl.resource.loader.api.client;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.resource.loader.api.ModPack;

/**
 * Events related to client side resource loading.
 */
public class ClientResourceLoaderEvents {

	/**
	 * This event is invoked upon game start-up, giving mod developers the
	 * opportunity to register custom built-in resource packs.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientResourceLoaderEvents.ADD_DEFAULT_RESOURCE_PACKS.register(adder -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Consumer<ModPack>>> ADD_DEFAULT_RESOURCE_PACKS = Event.consumer();

	/**
	 * This event is invoked before resources are reloaded.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientResourceLoaderEvents.START_RESOURCE_RELOAD.register(() -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Runnable> START_RESOURCE_RELOAD = Event.runnable();
	/**
	 * This event is invoked after resources are reloaded.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientResourceLoaderEvents.END_RESOURCE_RELOAD.register(() -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Runnable> END_RESOURCE_RELOAD   = Event.runnable();

}
