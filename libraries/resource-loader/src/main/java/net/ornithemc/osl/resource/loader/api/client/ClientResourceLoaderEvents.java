package net.ornithemc.osl.resource.loader.api.client;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.resource.loader.api.resource.manager.ReloadableResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadContext;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;

/**
 * Events related to client side resource loading.
 */
public final class ClientResourceLoaderEvents {

	/**
	 * This event is invoked when the ResourcePackRepository is initialized.
	 * Custom resource pack sources should be registered here.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientResourceLoaderEvents.INIT_RESOURCE_PACK_REPOSITORY.register(packRepository -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<ResourcePackRepository>> INIT_RESOURCE_PACK_REPOSITORY = Event.consumer();

	/**
	 * This event is invoked when the ResourceManager is initialized.
	 * Custom resource reloaders should be registered here.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientResourceLoaderEvents.INIT_RESOURCE_MANAGER.register(resourceManager -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<ReloadableResourceManager>> INIT_RESOURCE_MANAGER = Event.consumer();

	/**
	 * This event is invoked before resource packs are reloaded.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientResourceLoaderEvents.START_RESOURCE_PACKS_RELOAD.register(packRepository -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<ResourcePackRepository>> START_RESOURCE_PACKS_RELOAD = Event.consumer();

	/**
	 * This event is invoked after resource packs are reloaded.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientResourceLoaderEvents.END_RESOURCE_PACKS_RELOAD.register(packRepository -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<ResourcePackRepository>> END_RESOURCE_PACKS_RELOAD = Event.consumer();

	/**
	 * This event is invoked before resources are reloaded.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientResourceLoaderEvents.START_RESOURCE_RELOAD.register((resourceManager, context) -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<BiConsumer<ResourceManager, ResourceReloadContext>> START_RESOURCE_RELOAD = Event.biConsumer();

	/**
	 * This event is invoked after resources are reloaded.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientResourceLoaderEvents.END_RESOURCE_RELOAD.register((resourceManager, context) -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<BiConsumer<ResourceManager, ResourceReloadContext>> END_RESOURCE_RELOAD = Event.biConsumer();

}
