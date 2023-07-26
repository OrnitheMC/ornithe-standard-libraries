package net.ornithemc.osl.lifecycle.api;

import java.util.function.Consumer;

import net.minecraft.world.World;

import net.ornithemc.osl.core.api.events.Event;

public class WorldEvents {

	public static final Event<Consumer<World>> TICK_START = Event.consumer();
	public static final Event<Consumer<World>> TICK_END   = Event.consumer();

}
