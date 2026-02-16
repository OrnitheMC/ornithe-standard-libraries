package net.ornithemc.osl.branding.impl;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import net.ornithemc.osl.branding.api.BrandingContext;
import net.ornithemc.osl.branding.api.BrandingPatchEvents;
import net.ornithemc.osl.branding.api.Operation;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.launch.LaunchEvents;
import net.ornithemc.osl.entrypoints.api.launch.OptionsConsumer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;

public class BrandingPatchImpl implements ClientModInitializer {

	private static BrandingModifiers modifiers = new BrandingModifiers();

	public static String apply(BrandingContext context, String s) {
		return modifiers.apply(context, s);
	}

	@Override
	public void initClient() {
		LaunchEvents.PARSE_RUN_ARGS.register(new OptionsConsumer() {

			@Override
			public void defineOptions(OptionParser parser) {
			}

			@Override
			public void acceptOptions(OptionSet options) {
				String versionType = (String) options.valueOf(Constants.VERSION_TYPE);

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
