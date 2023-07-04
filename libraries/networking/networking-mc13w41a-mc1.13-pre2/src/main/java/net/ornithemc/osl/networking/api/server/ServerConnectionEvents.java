package net.ornithemc.osl.networking.api.server;

import java.util.function.BiConsumer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.core.api.events.Event;

public class ServerConnectionEvents {

	public static final Event<BiConsumer<MinecraftServer, ServerPlayerEntity>> LOGIN      = Event.biconsumer();
	public static final Event<BiConsumer<MinecraftServer, ServerPlayerEntity>> DISCONNECT = Event.biconsumer();

}
