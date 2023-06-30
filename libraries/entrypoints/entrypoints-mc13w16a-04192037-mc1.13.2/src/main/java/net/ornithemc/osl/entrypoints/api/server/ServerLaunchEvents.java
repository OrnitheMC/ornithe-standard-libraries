package net.ornithemc.osl.entrypoints.api.server;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

public final class ServerLaunchEvents {

	public static final Event<Consumer<String[]>> PARSE_RUN_ARGS = Event.consumer();

}
