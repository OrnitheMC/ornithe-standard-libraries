package net.ornithemc.osl.lifecycle.api.client;

import java.util.function.Consumer;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.client.Minecraft;

import net.ornithemc.osl.events.api.Event;

public class MinecraftClientEvents {

	public static final Event<RunArgsParser>  PRE_START = Event.of(listeners -> {
		return new RunArgsParser() {

			@Override
			public void defineOptions(OptionParser parser) {
				forEachListener(listener -> {
					listener.defineOptions(parser);
				});
			}

			@Override
			public void parseOptions(OptionSet options) {
				forEachListener(listener -> {
					listener.parseOptions(options);
				});
			}

			private void forEachListener(Consumer<RunArgsParser> action) {
				for (int i = 0; i < listeners.size(); i++) {
					action.accept(listeners.get(i));
				}
			}
		};
	});

	public static final Event<Consumer<Minecraft>> START     = Event.consumer();
	public static final Event<Consumer<Minecraft>> READY     = Event.consumer();
	public static final Event<Consumer<Minecraft>> STOP      = Event.consumer();

	public static final Event<Consumer<Minecraft>> TICK_START = Event.consumer();
	public static final Event<Consumer<Minecraft>> TICK_END   = Event.consumer();

	public static interface RunArgsParser {

		void defineOptions(OptionParser parser);

		void parseOptions(OptionSet options);

	}
}
