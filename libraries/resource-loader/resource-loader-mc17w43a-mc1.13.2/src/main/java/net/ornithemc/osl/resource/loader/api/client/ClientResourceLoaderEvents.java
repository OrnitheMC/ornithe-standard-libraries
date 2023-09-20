package net.ornithemc.osl.resource.loader.api.client;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.resource.loader.api.ModPack;

public class ClientResourceLoaderEvents {

	public static final Event<Consumer<Consumer<ModPack>>> ADD_DEFAULT_RESOURCE_PACKS = Event.consumer();

	public static final Event<Runnable> START_RESOURCE_RELOAD = Event.runnable();
	public static final Event<Runnable> END_RESOURCE_RELOAD   = Event.runnable();

}
