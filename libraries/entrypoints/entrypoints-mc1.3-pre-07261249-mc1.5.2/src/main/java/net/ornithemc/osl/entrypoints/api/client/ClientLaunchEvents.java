package net.ornithemc.osl.entrypoints.api.client;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.entrypoints.api.RunArgs;

public class ClientLaunchEvents {

	public static final Event<Consumer<RunArgs>> PARSE_RUN_ARGS = Event.consumer();

}
