package net.ornithemc.osl.entrypoints.api.client;

import java.util.function.Consumer;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import net.ornithemc.osl.events.api.Event;

public class ClientLaunchEvents {

	public static final Event<RunArgsParser>  PARSE_RUN_ARGS = Event.of(listeners -> {
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

	public static interface RunArgsParser {

		void defineOptions(OptionParser parser);

		void parseOptions(OptionSet options);

	}
}
