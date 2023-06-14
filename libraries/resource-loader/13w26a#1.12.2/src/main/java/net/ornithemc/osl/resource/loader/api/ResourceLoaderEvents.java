package net.ornithemc.osl.resource.loader.api;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

public class ResourceLoaderEvents {

	public static final Event<Consumer<Consumer<ModResourcePack>>> ADD_DEFAULT_RESOURCE_PACKS = Event.consumer();

	public static final Event<Runnable> START_RESOURCE_RELOAD = Event.runnable();
	public static final Event<Runnable> END_RESOURCE_RELOAD   = Event.runnable();

}
