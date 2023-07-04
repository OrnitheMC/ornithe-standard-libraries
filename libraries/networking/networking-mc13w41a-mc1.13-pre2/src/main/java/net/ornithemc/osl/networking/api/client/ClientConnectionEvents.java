package net.ornithemc.osl.networking.api.client;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.core.api.events.Event;

public class ClientConnectionEvents {

	public static final Event<Consumer<Minecraft>> LOGIN      = Event.consumer();
	public static final Event<Consumer<Minecraft>> DISCONNECT = Event.consumer();

}
