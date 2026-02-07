package net.ornithemc.osl.branding.impl;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import net.fabricmc.loader.api.FabricLoader;

import net.ornithemc.osl.branding.api.BrandingContext;
import net.ornithemc.osl.branding.api.BrandingPatchEvents;
import net.ornithemc.osl.branding.api.Operation;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.launch.LaunchEvents;
import net.ornithemc.osl.entrypoints.api.launch.OptionsConsumer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;

public class BrandingPatchImpl implements ClientModInitializer {

	private static String gameVersion = null;
	private static BrandingModifiers modifiers = new BrandingModifiers();

	public static String getGameVersion() {
		if (gameVersion == null) {
			gameVersion = FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString();
		}

		return gameVersion;
	}

	public static String apply(BrandingContext context, String s) {
		return modifiers.apply(context, s);
	}

	@Override
	public void initClient() {
		LaunchEvents.PARSE_RUN_ARGS.register(new OptionsConsumer() {

			private OptionSpec<String> versionSpec; // needed so that --version is not covered by --versionType
			private OptionSpec<String> versionTypeSpec;

			@Override
			public void defineOptions(OptionParser parser) {
				versionSpec = parser.accepts(Constants.VERSION).withRequiredArg();
				versionTypeSpec = parser.accepts(Constants.VERSION_TYPE).withRequiredArg().defaultsTo(Constants.RELEASE);
			}

			@Override
			public void acceptOptions(OptionSet options) {
				String versionType = options.valueOf(versionTypeSpec);

				if (versionType != null && !Constants.RELEASE.equals(versionType)) {
					modifiers.register(BrandingContext.ALL, Constants.VERSION_TYPE_COMPONENT, Operation.APPEND, "/" + versionType);
				}
			}
		});
		MinecraftClientEvents.START.register(minecraft -> {
			BrandingPatchEvents.REGISTER_MODIFIER_COMPONENT.invoker().accept(modifiers);
		});
	}
}
