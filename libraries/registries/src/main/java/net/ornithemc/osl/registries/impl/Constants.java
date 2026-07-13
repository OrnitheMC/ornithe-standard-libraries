package net.ornithemc.osl.registries.impl;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.ChannelIdentifiers;
import net.ornithemc.osl.networking.api.ChannelRegistry;

public final class Constants {

	public static final int REGISTRY_MAPPINGS_FORMAT = 1;
	public static final String REGISTRY_MAPPINGS_FILE_NAME = "registry_mappings.dat";
	public static final String FORMAT_NBT_KEY = "format";
	public static final String REGISTRIES_NBT_KEY = "registries";

	public static final NamespacedIdentifier OSL_REGISTRY_SYNC_CHANNEL = ChannelRegistry.register(ChannelIdentifiers.from("osl", "registries"));

}
