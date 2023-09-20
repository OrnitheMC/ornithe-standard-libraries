package net.ornithemc.osl.resource.loader.api.server;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.resource.loader.api.ModPack;

public class ServerResourceLoaderEvents {

	public static final Event<Consumer<Consumer<ModPack>>> ADD_DEFAULT_DATA_PACKS = Event.consumer();

	public static final Event<Runnable> START_RESOURCE_RELOAD = Event.runnable();
	public static final Event<Runnable> END_RESOURCE_RELOAD   = Event.runnable();

}