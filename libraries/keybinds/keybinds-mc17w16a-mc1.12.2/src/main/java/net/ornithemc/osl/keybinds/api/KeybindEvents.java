package net.ornithemc.osl.keybinds.api;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to Minecraft's keybinds.
 */
public final class KeybindEvents {

	/**
	 * This event is invoked upon game start-up, before game options are loaded.
	 * 
	 * <p>
	 * Custom keybind registration should be done in a listener of this event.
	 * Helper methods for registering keybinds can be found in the {@linkplain
	 * KeybindRegistry} class.
	 * 
	 * <p>
	 * Listeners to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * KeybindEvents.REGISTER_KEYBINDS.register(() -> {
	 * 	KeybindRegistry.register("cookie", Keyboard.KEY_Z, "example");
	 * });
	 * }
	 * </pre>
	 * 
	 * @see KeybindRegistry
	 */
	public static final Event<Runnable> REGISTER_KEYBINDS = Event.runnable();

}
