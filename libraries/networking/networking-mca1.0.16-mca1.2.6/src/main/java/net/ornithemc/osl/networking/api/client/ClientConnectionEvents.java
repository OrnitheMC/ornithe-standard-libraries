package net.ornithemc.osl.networking.api.client;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.text.api.TextComponent;

/**
 * Events related to the client side of a client-server connection.
 */
public final class ClientConnectionEvents {

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
	 * ClientConnectionEvents.LOGIN.register(context -> {
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
	 * ClientConnectionEvents.PLAY_READY.register(context -> {
	 * 	...
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<Consumer<PlayReadyContext>> PLAY_READY = Event.consumer();
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
	 * ClientConnectionEvents.DISCONNECT.register(context -> {
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
		 * @return the current {@linkplain Minecraft} instance.
		 */
		Minecraft minecraft();

		/**
		 * @return the name of the selected singleplayer world, or {@code null} if connected to a remote server.
		 */
		String worldName();

		/**
		 * @return the address of the server logged into, or {@code null} if selected a singleplayer world.
		 */
		String serverAddress();

		/**
		 * @return the port of the server logged into, or {@code -1} if selected a singleplayer world.
		 */
		int serverPort();

		/**
		 * @return whether the server is a local integrated server.
		 */
		boolean isServerLocal();

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
