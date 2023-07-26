package net.ornithemc.osl.lifecycle.api;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.core.api.events.Event;

public final class MinecraftEvents {

	public static final Event<Consumer<Minecraft>> START = Event.consumer();
	public static final Event<Consumer<Minecraft>> READY = Event.consumer();
	public static final Event<Consumer<Minecraft>> STOP  = Event.consumer();

	public static final Event<Consumer<Minecraft>> TICK_START = Event.consumer();
	public static final Event<Consumer<Minecraft>> TICK_END   = Event.consumer();

	public static final Event<Consumer<Minecraft>> LOAD_WORLD    = Event.consumer();
	public static final Event<Consumer<Minecraft>> PREPARE_WORLD = Event.consumer();
	public static final Event<Consumer<Minecraft>> READY_WORLD   = Event.consumer();

}
