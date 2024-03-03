package net.ornithemc.osl.branding.impl;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import net.ornithemc.osl.branding.api.BrandingContext;
import net.ornithemc.osl.branding.api.BrandingPatchEvents;
import net.ornithemc.osl.branding.api.Operation;
import net.ornithemc.osl.entrypoints.api.RunArgsConsumer;
import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;

public class BrandingPatchImpl implements ClientModInitializer {

	private static BrandingModifiers modifiers = new BrandingModifiers();

	public static String apply(BrandingContext context, String s) {
		return modifiers.apply(context, s);
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
