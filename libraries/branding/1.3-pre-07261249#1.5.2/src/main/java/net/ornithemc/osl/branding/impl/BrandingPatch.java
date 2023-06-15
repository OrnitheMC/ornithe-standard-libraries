package net.ornithemc.osl.branding.impl;

import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class BrandingPatch implements ClientModInitializer {

	public static String versionType = "release";

	public static String apply(String s) {
		if (versionType == null || "release".equals(versionType)) {
			return s;
		}

		return s + "/" + versionType;
	}

	@Override
	public void initClient() {
		ClientLaunchEvents.PARSE_RUN_ARGS.register(args -> {
			for (int i = 0; i < args.length - 1; i++) {
				if ("-versionType".equals(args[i]) || "--versionType".equals(args[i])) {
					versionType = args[i + 1];
					break;
				}
			}
		});
	}
}
