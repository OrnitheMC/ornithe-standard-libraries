package net.ornithemc.osl.lifecycle.api;

import java.util.function.Consumer;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events to track the lifecycle of the Minecraft game.
 */
public class MinecraftEvents {

	/**
	 * This event is invoked upon game start-up, before the server is initialized.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftEvents.START.register(server -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<MinecraftServer>> START = Event.consumer();
	/**
	 * This event is invoked upon game start-up, after the server is initialized.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftEvents.READY.register(server -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<MinecraftServer>> READY = Event.consumer();
	/**
	 * This event is invoked upon game shut-down.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftEvents.STOP.register(server -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<MinecraftServer>> STOP = Event.consumer();

	/**
	 * This event is invoked each tick, before the server ticks.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftEvents.TICK_START.register(server -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<MinecraftServer>> TICK_START = Event.consumer();
	/**
	 * This event is invoked each tick, after the server ticks.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftEvents.TICK_END.register(server -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<MinecraftServer>> TICK_END = Event.consumer();

	/**
	 * This event is invoked before the world save is loaded.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftEvents.LOAD_WORLD.register(server -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<MinecraftServer>> LOAD_WORLD = Event.consumer();
	/**
	 * This event is invoked after the world save is loaded, but before the
	 * spawn region has been loaded.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftEvents.PREPARE_WORLD.register(server -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<MinecraftServer>> PREPARE_WORLD = Event.consumer();
	/**
	 * This event is invoked after the world save is loaded, after the
	 * spawn region has been loaded.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftEvents.READY_WORLD.register(server -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<MinecraftServer>> READY_WORLD = Event.consumer();

}
