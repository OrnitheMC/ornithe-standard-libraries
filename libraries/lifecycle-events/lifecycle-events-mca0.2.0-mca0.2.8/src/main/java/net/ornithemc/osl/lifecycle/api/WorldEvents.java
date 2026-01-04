package net.ornithemc.osl.lifecycle.api;

import java.util.function.Consumer;

import net.minecraft.world.World;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events to track the lifecycle of a Minecraft world.
 */
public class WorldEvents {

	/**
	 * This event is invoked each tick, before the world ticks.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * WorldEvents.TICK_START.register(world -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<World>> TICK_START = Event.consumer();
	/**
	 * This event is invoked each tick, after the world ticks.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * WorldEvents.TICK_START.register(world -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<World>> TICK_END = Event.consumer();

}
