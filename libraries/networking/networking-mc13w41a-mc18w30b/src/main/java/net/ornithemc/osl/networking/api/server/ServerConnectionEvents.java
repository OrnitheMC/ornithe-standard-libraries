package net.ornithemc.osl.networking.api.server;

import java.util.function.BiConsumer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events related to the server side of a client-server connection.
 */
public class ServerConnectionEvents {

	/**
	 * This event is fired after a successful login occurs.
	 * 
	 * <p>
	 * Note that channel registration happens after login,
	 * and until then data cannot safely be sent to the client.
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
	 * ServerConnectionEvents.LOGIN.register((server, player) -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<BiConsumer<MinecraftServer, ServerPlayerEntity>> LOGIN = Event.biConsumer();
	/**
	 * This event is fired after login, once channel registration is complete.
	 * 
	 * <p>
	 * This marks the moment data can safely be sent to the client.
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
	 * ServerConnectionEvents.PLAY_READY.register((server, player) -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<BiConsumer<MinecraftServer, ServerPlayerEntity>> PLAY_READY = Event.biConsumer();
	/**
	 * This event is fired when a client disconnects from the server.
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
	 * ServerConnectionEvents.DISCONNECT.register((server, player) -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<BiConsumer<MinecraftServer, ServerPlayerEntity>> DISCONNECT = Event.biConsumer();

}
