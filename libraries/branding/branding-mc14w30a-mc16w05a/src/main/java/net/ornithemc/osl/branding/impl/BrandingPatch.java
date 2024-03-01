package net.ornithemc.osl.branding.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import net.ornithemc.osl.branding.api.BrandingModifier;
import net.ornithemc.osl.branding.api.BrandingPatchEvents;
import net.ornithemc.osl.entrypoints.api.RunArgsConsumer;
import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;

public class BrandingPatch implements ClientModInitializer {

	private static Set<BrandingModifierImpl> modifiers = new LinkedHashSet<>();

	public static String apply(String s) {
		String original = s;

		for (BrandingModifierImpl modifier : modifiers) {
			s = modifier.apply(original, s);
		}

		return s;
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
					registerModifier(BrandingModifier.APPEND, versionType);
				}
			}
		});
		MinecraftClientEvents.START.register(minecraft -> {
			BrandingPatchEvents.REGISTER_MODIFIER.invoker().accept(BrandingPatch::registerModifier);
		});
	}

	private static void registerModifier(BrandingModifier modifier, String value) {
		if (!modifiers.add(new BrandingModifierImpl(modifier, value))) {
			throw new IllegalStateException("cannot register multiple REPLACE branding modifiers!");
		}
	}
}
