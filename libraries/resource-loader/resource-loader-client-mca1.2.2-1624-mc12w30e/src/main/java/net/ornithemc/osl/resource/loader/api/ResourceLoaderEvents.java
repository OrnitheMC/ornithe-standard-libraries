package net.ornithemc.osl.resource.loader.api;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to resource loading.
 */
public class ResourceLoaderEvents {

	/**
	 * This event is invoked upon game start-up, giving mod developers the
	 * opportunity to register custom built-in texture packs.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ResourceLoaderEvents.ADD_DEFAULT_TEXTURE_PACKS.register(adder -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Consumer<ModTexturePack>>> ADD_DEFAULT_TEXTURE_PACKS = Event.consumer();

	/**
	 * This event is invoked before resources are reloaded.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ResourceLoaderEvents.START_RESOURCE_RELOAD.register(() -> {
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
	 * ResourceLoaderEvents.END_RESOURCE_RELOAD.register(() -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Runnable> END_RESOURCE_RELOAD   = Event.runnable();

}
