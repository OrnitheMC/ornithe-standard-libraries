package net.ornithemc.osl.lifecycle.api.client;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events to track the lifecycle of the Minecraft client.
 */
public class MinecraftClientEvents {

	/**
	 * This event is invoked upon game start-up, before the client is initialized.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftClientEvents.START.register(minecraft -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Minecraft>> START = Event.consumer();
	/**
	 * This event is invoked upon game start-up, after the client is initialized.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftClientEvents.READY.register(minecraft -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Minecraft>> READY = Event.consumer();
	/**
	 * This event is invoked upon game shut-down.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftClientEvents.STOP.register(minecraft -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Minecraft>> STOP = Event.consumer();

	/**
	 * This event is invoked each tick, before the client ticks.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftClientEvents.TICK_START.register(minecraft -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Minecraft>> TICK_START = Event.consumer();
	/**
	 * This event is invoked each tick, after the client ticks.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftClientEvents.TICK_END.register(minecraft -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Minecraft>> TICK_END = Event.consumer();

}
