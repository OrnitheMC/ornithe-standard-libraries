package net.ornithemc.osl.lifecycle.api.server;

import java.util.function.Consumer;

import net.minecraft.server.world.ServerWorld;

import net.ornithemc.osl.core.api.events.Event;

public class ServerWorldEvents {

	public static final Event<Consumer<ServerWorld>> INIT = Event.consumer();

	public static final Event<Consumer<ServerWorld>> TICK_START = Event.consumer();
	public static final Event<Consumer<ServerWorld>> TICK_END   = Event.consumer();

}
