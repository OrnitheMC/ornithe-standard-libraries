package net.ornithemc.osl.lifecycle.api.server;

import java.util.function.Consumer;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.events.api.Event;

public final class MinecraftServerEvents {

	public static final Event<Consumer<String[]>> PRE_START = Event.consumer();

	public static final Event<Consumer<MinecraftServer>> START = Event.consumer();
	public static final Event<Consumer<MinecraftServer>> READY = Event.consumer();
	public static final Event<Consumer<MinecraftServer>> STOP  = Event.consumer();

	public static final Event<Consumer<MinecraftServer>> TICK_START = Event.consumer();
	public static final Event<Consumer<MinecraftServer>> TICK_END   = Event.consumer();

}
