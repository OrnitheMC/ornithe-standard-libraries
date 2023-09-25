package net.ornithemc.osl.lifecycle.api;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events to track the lifecycle of the Minecraft game.
 */
public final class MinecraftEvents {

	/**
	 * This event is invoked upon game start-up, before the client is initialized.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * MinecraftEvents.START.register(minecraft -> {
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
	 * MinecraftEvents.READY.register(minecraft -> {
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
	 * MinecraftEvents.STOP.register(minecraft -> {
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
	 * MinecraftEvents.TICK_START.register(minecraft -> {
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
	 * MinecraftEvents.TICK_END.register(minecraft -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Minecraft>> TICK_END = Event.consumer();

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
	public static final Event<Consumer<Minecraft>> LOAD_WORLD = Event.consumer();
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
	public static final Event<Consumer<Minecraft>> PREPARE_WORLD = Event.consumer();
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
	public static final Event<Consumer<Minecraft>> READY_WORLD = Event.consumer();

}
