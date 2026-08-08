package net.ornithemc.osl.entities.api.client;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the entities rendering lifecycle.
 */
public final class EntityRenderingEvents {

	/**
	 * This event is invoked upon client start-up, after Vanilla entity renderer registration is finished.
	 * 
	 * <p>
	 * Custom entity renderer registration should be done in a listener of this event.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * EntityRenderingEvents.REGISTER_ENTITY_RENDERERS.register(registry -> {
	 * 	registry.register(CookieMonsterEntity.class, CookieMonsterRenderer::new);
	 * });
	 * }
	 * </pre>
	 * 
	 * @see EntityRendererRegistry
	 */
	public static final Event<Consumer<EntityRendererRegistry>> REGISTER_ENTITY_RENDERERS = Event.consumer();

}
