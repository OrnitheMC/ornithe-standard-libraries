package net.ornithemc.osl.entrypoints.api.launch;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import net.ornithemc.osl.core.api.events.Event;

/**
 * Events to track the launch cycle of Minecraft.
 */
public class LaunchEvents {

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
	 * LaunchEvents.PARSE_RUN_ARGS.register(new OptionsConsumer() {
	 * 	private ArgumentAcceptingOptionSpec<String> cookieSpec;
	 * 	@Override
	 * 	public void defineOptions(OptionParser parser) {
	 * 		cookieSpec = parser.accepts("cookie").withRequiredArg();
	 * 	}
	 * 	@Override
	 * 	public void acceptOptions(OptionSet options) {
	 * 		String param = options.valueOf(cookieSpec);
	 * 		...
	 * 	}
	 * });
	 * }
	 * </pre>
	 */
	public static final Event<OptionsConsumer> PARSE_RUN_ARGS = Event.of(listeners -> {
		return new OptionsConsumer() {

			@Override
			public void defineOptions(OptionParser parser) {
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).defineOptions(parser);
				}
			}

			@Override
			public void acceptOptions(OptionSet options) {
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).acceptOptions(options);
				}
			}
		};
	});
}
