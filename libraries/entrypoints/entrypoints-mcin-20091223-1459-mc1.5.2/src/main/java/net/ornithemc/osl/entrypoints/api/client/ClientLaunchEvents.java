package net.ornithemc.osl.entrypoints.api.client;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.entrypoints.api.RunArgs;

/**
 * Events to track the launch cycle of the Minecraft client.
 */
public class ClientLaunchEvents {

	/**
	 * This event is invoked before the game is initialized, giving
	 * mod developers the opportunity to parse custom run args.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientLaunchEvents.PARSE_RUN_ARGS.register(args -> {
	 * 	String param = args.getParameter("cookie");
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<RunArgs>> PARSE_RUN_ARGS = Event.consumer();

}
