package net.ornithemc.osl.entities.api;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the entities lifecycle.
 */
public final class EntityEvents {

	/**
	 * This event is invoked upon game start-up, after Vanilla entity registration is finished.
	 * 
	 * <p>
	 * Custom entity type registration should be done in a listener of this event.
	 * Helper methods for registering entity types can be found in the {@linkplain
	 * EntityTypeRegistry} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * EntityEvents.REGISTER_ENTITY_TYPES.register(() -> {
	 * 	EntityTypeRegistry.register(NamespacedIdentifiers.from("example", "cookie_monster"), CookieMonsterEntity.class);
	 * });
	 * }
	 * </pre>
	 * 
	 * @see EntityTypeRegistry
	 */
	public static final Event<Runnable> REGISTER_ENTITY_TYPES = Event.runnable();

}
