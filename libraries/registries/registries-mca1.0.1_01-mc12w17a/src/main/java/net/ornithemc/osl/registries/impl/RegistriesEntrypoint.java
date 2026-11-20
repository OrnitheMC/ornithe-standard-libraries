package net.ornithemc.osl.registries.impl;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.registries.impl.mixin.client.ClientNetworkHandlerAccess;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingException;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingSource;
import net.ornithemc.osl.registries.impl.registry.sync.SyncedRegistriesPacketSerializer;

public final class RegistriesEntrypoint implements ClientModInitializer {

	public static final boolean OLD_WORLD_FORMAT = MinecraftVersion.resolve().compareTo("b1.3") < 0;
	public static final boolean CLIENT_SUPPORTS_NETHER = MinecraftVersion.resolve().compareTo("a1.2.0") >= 0;
	public static final boolean SERVER_SUPPORTS_NETHER = MinecraftVersion.resolve().compareTo("b1.5_02") > 0;

	@Override
	public void initClient() {
		ClientPlayNetworking.registerListener(Constants.OSL_REGISTRY_SYNC_CHANNEL, (context, buffer) -> {
			try {
				SyncedRegistriesPacketSerializer.deserialize(buffer, RegistryMappingSource.REMOTE_SERVER);

				context.minecraft().execute(() -> {
					SyncedRegistriesImpl.applyMappings();
				});
			} catch (RegistryMappingException e) {
				RegistriesImpl.LOGGER.error("Unable to remap registries!", e);

				context.minecraft().execute(() -> {
					SyncedRegistriesImpl.resetMappings();

					if (!((ClientNetworkHandlerAccess) context.networkHandler()).accessDisconnected()) {
						((ClientNetworkHandlerAccess) context.networkHandler()).accessConnection().close("Failed to remap registries: " + e.getMessage());
					}
				});
			}
		});
		ClientConnectionEvents.DISCONNECT.register(context -> {
			if (context.isMultiplayer()) {
				SyncedRegistriesImpl.undoMappings();
			}
		});
	}
}
