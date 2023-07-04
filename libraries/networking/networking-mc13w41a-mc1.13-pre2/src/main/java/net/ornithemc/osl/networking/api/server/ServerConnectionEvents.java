package net.ornithemc.osl.networking.api.server;

import java.util.function.BiConsumer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.core.api.events.Event;

public class ServerConnectionEvents {

	/**
	 * This event is fired after a successful login occurs.
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 */
	public static final Event<BiConsumer<MinecraftServer, ServerPlayerEntity>> LOGIN = Event.biConsumer();
	/**
	 * This event is fired when a client disconnects from the server.
	 * This applies to connections to dedicated servers as
	 * well as connections to integrated servers.
	 */
	public static final Event<BiConsumer<MinecraftServer, ServerPlayerEntity>> DISCONNECT = Event.biConsumer();

}
