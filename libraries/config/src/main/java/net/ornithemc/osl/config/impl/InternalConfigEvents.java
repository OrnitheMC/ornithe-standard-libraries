package net.ornithemc.osl.config.impl;

import java.util.function.Consumer;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.core.api.events.Event;

public class InternalConfigEvents {

	public static final Event<Consumer<ConfigScope>> CONFIG_MANAGER_READY = Event.consumer();

}
