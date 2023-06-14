package net.ornithemc.osl.lifecycle.api.client;

import java.util.function.Consumer;

import net.minecraft.client.world.ClientWorld;

import net.ornithemc.osl.core.api.events.Event;

public class ClientWorldEvents {

	public static final Event<Consumer<ClientWorld>> TICK_START = Event.consumer();
	public static final Event<Consumer<ClientWorld>> TICK_END   = Event.consumer();

}
