package net.ornithemc.osl.blockentities.api.client;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the block entities rendering lifecycle.
 */
public final class BlockEntityRenderingEvents {

	/**
	 * This event is invoked upon client start-up, after Vanilla block entity renderer registration is finished.
	 * 
	 * <p>
	 * Custom block entity renderer registration should be done in a listener of this event.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * BlockEntityRenderingEvents.REGISTER_BLOCK_ENTITY_RENDERERS.register(registry -> {
	 * 	registry.register(CookieBlockEntity.class, CookieBlockRenderer::new);
	 * });
	 * }
	 * </pre>
	 * 
	 * @see BlockEntityRendererRegistry
	 */
	public static final Event<Consumer<BlockEntityRendererRegistry>> REGISTER_BLOCK_ENTITY_RENDERERS = Event.consumer();

}
