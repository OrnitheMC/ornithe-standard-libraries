package net.ornithemc.osl.keybinds.api;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

public class KeyBindingEvents {

	public static final Event<Consumer<KeyBindingRegistry>> REGISTER_KEYBINDS = Event.consumer();

}
