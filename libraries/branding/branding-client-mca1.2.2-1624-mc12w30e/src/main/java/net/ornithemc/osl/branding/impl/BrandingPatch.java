package net.ornithemc.osl.branding.impl;

import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents;
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
		ClientLaunchEvents.PARSE_RUN_ARGS.register(args -> {
			String versionTypeArg = args.getParameter(Constants.VERSION_TYPE);

			if (versionTypeArg != null) {
				versionType = versionTypeArg;
			}
		});
	}
}
