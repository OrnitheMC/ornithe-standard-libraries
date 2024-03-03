package net.ornithemc.osl.branding.api;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the patching of branding information within several areas of the game.
 */
public class BrandingPatchEvents {

	/**
	 * This event is invoked upon game start-up, giving mod developers
	 * the opportunity to register custom branding modifier components.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * BrandingPatchEvents.REGISTER_MODIFIER_COMPONENT.register(registry -> {
	 * 	registry.register(BrandingContext.ALL, "Cookie", Operation.APPEND, "/Cookie");
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<BrandingModifierRegistry>> REGISTER_MODIFIER_COMPONENT = Event.consumer();

}
