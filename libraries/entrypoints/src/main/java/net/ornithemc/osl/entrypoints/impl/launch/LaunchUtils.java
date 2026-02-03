package net.ornithemc.osl.entrypoints.impl.launch;

import joptsimple.OptionParser;

import net.ornithemc.osl.entrypoints.api.launch.LaunchEvents;

public class LaunchUtils {

	public static void triggerLaunchEvents(String[] args) {
		OptionParser parser = new OptionParser();
		parser.allowsUnrecognizedOptions();

		LaunchEvents.PARSE_RUN_ARGS.invoker().defineOptions(parser);
		LaunchEvents.PARSE_RUN_ARGS.invoker().acceptOptions(parser.parse(args));
	}
}
