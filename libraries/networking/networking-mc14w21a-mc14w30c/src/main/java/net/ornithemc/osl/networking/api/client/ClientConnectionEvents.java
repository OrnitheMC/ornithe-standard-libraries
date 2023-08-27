package net.ornithemc.osl.networking.api.client;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.core.api.events.Event;

public class ClientConnectionEvents {

	/**
	 * This event is fired after a successful login occurs.
	 * Note that channel registration happens after login,
	 * and until then data cannot safely be sent to the server.
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 */
	public static final Event<Consumer<Minecraft>> LOGIN = Event.consumer();
	/**
	 * This event is fired after login, once channel registration is complete.
	 * This marks the moment data can safely be sent to the server.
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 */
	public static final Event<Consumer<Minecraft>> PLAY_READY = Event.consumer();
	/**
	 * This event is fired when the client disconnects from the server.
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 */
	public static final Event<Consumer<Minecraft>> DISCONNECT = Event.consumer();

}
