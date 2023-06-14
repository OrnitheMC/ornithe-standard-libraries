package net.ornithemc.osl.entrypoints.api.client;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

public class ClientLaunchEvents {

	public static final Event<Consumer<String[]>> PARSE_RUN_ARGS = Event.consumer();

}
