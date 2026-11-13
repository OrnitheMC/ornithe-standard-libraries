package net.ornithemc.osl.lifecycle.api.server;

import java.util.function.Consumer;

import net.minecraft.server.world.ServerWorld;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events to track the lifecycle of a server world.
 */
public class ServerWorldEvents {

	/**
	 * This event is invoked each tick, before the world ticks.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ServerWorldEvents.TICK_START.register(world -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<ServerWorld>> TICK_START = Event.consumer();
	/**
	 * This event is invoked each tick, after the world ticks.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ServerWorldEvents.TICK_START.register(world -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<ServerWorld>> TICK_END = Event.consumer();

}
