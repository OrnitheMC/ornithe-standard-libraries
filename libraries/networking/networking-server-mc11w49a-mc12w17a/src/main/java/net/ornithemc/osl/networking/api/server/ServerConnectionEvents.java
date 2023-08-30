package net.ornithemc.osl.networking.api.server;

import java.util.function.BiConsumer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.core.api.events.Event;

public class ServerConnectionEvents {

	/**
	 * This event is fired after a successful login occurs.
	 * Note that channel registration happens after login,
	 * and until then data cannot safely be sent to the client.
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 */
	public static final Event<BiConsumer<MinecraftServer, ServerPlayerEntity>> LOGIN = Event.biConsumer();
	/**
	 * This event is fired after login, once channel registration is complete.
	 * This marks the moment data can safely be sent to the client.
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 */
	public static final Event<BiConsumer<MinecraftServer, ServerPlayerEntity>> PLAY_READY = Event.biConsumer();
	/**
	 * This event is fired when a client disconnects from the server.
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 */
	public static final Event<BiConsumer<MinecraftServer, ServerPlayerEntity>> DISCONNECT = Event.biConsumer();

}
