package net.ornithemc.osl.lifecycle.api.client;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.core.api.events.Event;

public class MinecraftClientEvents {

	public static final Event<Consumer<Minecraft>> START = Event.consumer();
	public static final Event<Consumer<Minecraft>> READY = Event.consumer();
	public static final Event<Consumer<Minecraft>> STOP  = Event.consumer();

	public static final Event<Consumer<Minecraft>> TICK_START = Event.consumer();
	public static final Event<Consumer<Minecraft>> TICK_END   = Event.consumer();

}
