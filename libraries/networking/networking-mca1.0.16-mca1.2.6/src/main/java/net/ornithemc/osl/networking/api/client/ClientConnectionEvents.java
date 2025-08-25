package net.ornithemc.osl.networking.api.client;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the client side of a client-server connection.
 */
public class ClientConnectionEvents {

	/**
	 * This event is fired after a successful login occurs.
	 * 
	 * <p>
	 * Note that channel registration happens after login,
	 * and until then data cannot safely be sent to the server.
	 * 
	 * <p>
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientConnectionEvents.LOGIN.register(minecraft -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Minecraft>> LOGIN = Event.consumer();
	/**
	 * This event is fired after login, once channel registration is complete.
	 * 
	 * <p>
	 * This marks the moment data can safely be sent to the server.
	 * 
	 * <p>
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientConnectionEvents.PLAY_READY.register(minecraft -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Minecraft>> PLAY_READY = Event.consumer();
	/**
	 * This event is fired when the client disconnects from the server.
	 * 
	 * <p>
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientConnectionEvents.DISCONNECT.register(minecraft -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<Minecraft>> DISCONNECT = Event.consumer();

}
