package net.ornithemc.osl.registries.impl;

import net.minecraft.text.LiteralText;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingException;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingSource;
import net.ornithemc.osl.registries.impl.registry.sync.SyncedRegistriesPacketSerializer;

public final class RegistriesEntrypoint implements ClientModInitializer {

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

					if (context.networkHandler().getConnection().isConnected()) {
						context.networkHandler().getConnection().disconnect(new LiteralText("Failed to remap registries: " + e.getMessage()));
					}
				});
			}
		});
		ClientConnectionEvents.DISCONNECT.register(context -> {
			if (!context.isServerLocal()) {
				SyncedRegistriesImpl.undoMappings();
			}
		});
	}
}
