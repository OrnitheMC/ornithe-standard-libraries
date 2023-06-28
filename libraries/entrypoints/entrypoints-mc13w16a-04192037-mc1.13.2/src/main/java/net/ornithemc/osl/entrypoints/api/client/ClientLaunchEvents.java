package net.ornithemc.osl.entrypoints.api.client;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import net.ornithemc.osl.core.api.events.Event;

public class ClientLaunchEvents {

	public static final Event<RunArgsConsumer> PARSE_RUN_ARGS = Event.of(listeners -> {
		return new RunArgsConsumer() {

			@Override
			public void defineOptions(OptionParser parser) {
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).defineOptions(parser);
				}
			}

			@Override
			public void parseOptions(OptionSet options) {
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).parseOptions(options);
				}
			}
		};
	});

	public static interface RunArgsConsumer {

		void defineOptions(OptionParser parser);

		void parseOptions(OptionSet options);

	}
}
