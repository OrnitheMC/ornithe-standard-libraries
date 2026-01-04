package net.ornithemc.osl.entrypoints.api.server;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events to track the launch cycle of the dedicated Minecraft server.
 */
public final class ServerLaunchEvents {

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
	 * ServerLaunchEvents.PARSE_RUN_ARGS.register(args -> {
	 * 	for (int i = 0; i < args.length; i++) {
	 * 		if ("cookie".equals(args[i])) {
	 * 			String param = args[++i];
	 * 			...
	 * 		}
	 * 	}
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<String[]>> PARSE_RUN_ARGS = Event.consumer();

}
