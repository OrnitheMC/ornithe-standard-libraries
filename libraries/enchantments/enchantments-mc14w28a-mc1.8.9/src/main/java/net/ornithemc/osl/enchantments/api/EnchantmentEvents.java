package net.ornithemc.osl.enchantments.api;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the enchantments lifecycle.
 */
public final class EnchantmentEvents {

	/**
	 * This event is invoked upon game start-up, after Vanilla enchantment registration is finished.
	 * 
	 * <p>
	 * Custom enchantment registration should be done in a listener of this event.
	 * Helper methods for registering enchantments can be found in the {@linkplain
	 * EnchantmentRegistry} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * EnchantmentEvents.REGISTER_ENCHANTMENTS.register(() -> {
	 * 	EnchantmentRegistry.register(NamespacedIdentifiers.from("example", "cookie"), new CookieEnchantment(Enchantment.AUTO_ASSIGN_ID));
	 * });
	 * }
	 * </pre>
	 * 
	 * @see EnchantmentRegistry
	 */
	public static final Event<Runnable> REGISTER_ENCHANTMENTS = Event.runnable();

}
