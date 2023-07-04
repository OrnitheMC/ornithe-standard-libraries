package net.ornithemc.osl.networking.api.client;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.core.api.events.Event;

public class ClientConnectionEvents {

	/**
	 * This event is fired after a successful login occurs.
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 */
	public static final Event<Consumer<Minecraft>> LOGIN = Event.consumer();
	/**
	 * This event is fired when the client disconnects from the server.
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 */
	public static final Event<Consumer<Minecraft>> DISCONNECT = Event.consumer();

}
