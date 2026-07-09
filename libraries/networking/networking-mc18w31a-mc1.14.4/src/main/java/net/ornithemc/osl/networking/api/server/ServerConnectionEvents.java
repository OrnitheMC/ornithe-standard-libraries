package net.ornithemc.osl.networking.api.server;

import java.util.function.Consumer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.text.api.TextComponent;

/**
 * Events related to the server side of a client-server connection.
 */
public final class ServerConnectionEvents {

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
	 * ServerConnectionEvents.LOGIN.register(context -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<LoginContext>> LOGIN = Event.consumer();
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
	 * ServerConnectionEvents.PLAY_READY.register(context -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<PlayReadyContext>> PLAY_READY = Event.consumer();
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
	 * ServerConnectionEvents.DISCONNECT.register(context -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<DisconnectContext>> DISCONNECT = Event.consumer();

	/**
	 * Common interface for connection context classes.
	 */
	public interface ConnectionContext {

		/**
		 * @return the current {@linkplain MinecraftServer} instance.
		 */
		MinecraftServer server();

		/**
		 * @return the current {@linkplain ServerPlayerEntity} instance.
		 */
		ServerPlayerEntity player();

	}

	/**
	 * Access to relevant context for login events.
	 */
	public interface LoginContext extends ConnectionContext {
	}

	/**
	 * Access to relevant context for play ready events.
	 */
	public interface PlayReadyContext extends ConnectionContext {
	}

	/**
	 * Access to relevant context for disconnect events.
	 */
	public interface DisconnectContext extends ConnectionContext {

		/**
		 * @return the reason for the disconnect, or {@code null} if disconnected by leaving the server or closing the game.
		 */
		TextComponent disconnectReason();

	}
}
