package net.ornithemc.osl.entrypoints.api.client;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.entrypoints.api.RunArgsConsumer;

/**
 * Events to track the launch cycle of the Minecraft client.
 */
public class ClientLaunchEvents {

	/**
	 * This event is invoked before the game is initialized, giving
	 * mod developers the opportunity to parse custom run args.
	 * 
	 * <p>
	 * Callbacks to this event should be registered in your mod's entrypoint,
	 * and can be done as follows:
	 * 
	 * <pre>
	 * {@code
	 * ClientLaunchEvents.PARSE_RUN_ARGS.register(new RunArgsConsumer() {
	 * 	private ArgumentAcceptingOptionSpec<String> cookieSpec;
	 * 	@Override
	 * 	public void defineOptions(OptionParser parser) {
	 * 		cookieSpec = parser.accepts("cookie").withRequiredArg();
	 * 	}
	 * 	@Override
	 * 	public void parseOptions(OptionSet options) {
	 * 		String param = options.valueOf(cookieSpec);
	 * 		...
	 * 	}
	 * });
	 * }
	 * </pre>
	 */
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
}
