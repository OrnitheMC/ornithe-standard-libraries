package net.ornithemc.osl.lifecycle.api;

import net.ornithemc.osl.core.api.events.Event;

public class LifecycleEvents {

	public static final Event<Runnable> BOOTSTRAP_START = Event.runnable();
	public static final Event<Runnable> BOOTSTRAP_END   = Event.runnable();

}
