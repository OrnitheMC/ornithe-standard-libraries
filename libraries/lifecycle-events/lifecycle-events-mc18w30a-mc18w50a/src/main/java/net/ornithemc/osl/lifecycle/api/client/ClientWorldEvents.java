package net.ornithemc.osl.lifecycle.api.client;

import java.util.function.Consumer;

import net.minecraft.client.world.ClientWorld;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events to track the lifecycle of a client world.
 */
public class ClientWorldEvents {

	/**
	 * This event is invoked each tick, before the world ticks.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientWorldEvents.TICK_START.register(world -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<ClientWorld>> TICK_START = Event.consumer();
	/**
	 * This event is invoked each tick, after the world ticks.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientWorldEvents.TICK_START.register(world -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<ClientWorld>> TICK_END = Event.consumer();

}
