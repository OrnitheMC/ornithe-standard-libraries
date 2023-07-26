package net.ornithemc.osl.branding.impl;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents.RunArgsConsumer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class BrandingPatch implements ClientModInitializer {

	private static String versionType = Constants.RELEASE;

	public static String apply(String s) {
		if (versionType == null || Constants.RELEASE.equals(versionType)) {
			return s;
		}

		return s + "/" + versionType;
	}

	@Override
	public void initClient() {
		ClientLaunchEvents.PARSE_RUN_ARGS.register(new RunArgsConsumer() {

			private OptionSpec<String> versionTypeSpec;

			@Override
			public void defineOptions(OptionParser parser) {
				versionTypeSpec = parser.accepts(Constants.VERSION_TYPE).withRequiredArg().defaultsTo(Constants.RELEASE);
			}

			@Override
			public void parseOptions(OptionSet options) {
				versionType = options.valueOf(versionTypeSpec);
			}
		});
	}
}
