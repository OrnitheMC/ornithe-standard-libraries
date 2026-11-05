package net.ornithemc.osl.registries.impl.registry.sync;

import java.io.IOException;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.impl.Constants;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistry;

public final class SyncedRegistriesPacketSerializer {

	private static final Registry<SyncedRegistry> REGISTRIES = SyncedRegistriesImpl.SYNCED_REGISTRIES;

	public static void serialize(PacketBuffer buffer) throws IOException {
		buffer.writeVarInt(Constants.REGISTRY_MAPPINGS_FORMAT);
		buffer.writeVarInt(REGISTRIES.keySet().size());

		for (SyncedRegistry registry : REGISTRIES) {
			NamespacedIdentifier identifier = registry.identifier();
			SerializableRegistryMappings mappings = registry.getMappings();

			buffer.writeNamespacedIdentifier(identifier);
			mappings.write(buffer, SyncedRegistriesPacketSerializer::serialize);
		}
	}

	public static void deserialize(PacketBuffer buffer, RegistryMappingSource source) throws IOException, RegistryMappingException {
		int format = buffer.readVarInt();

		if (format > Constants.REGISTRY_MAPPINGS_FORMAT) {
			throw new IllegalStateException("cannot read registry mappings of newer format " + format);
		}

		int size = buffer.readVarInt();

		for (int i = 0; i < size; i++) {
			NamespacedIdentifier identifier = buffer.readNamespacedIdentifier();
			SyncedRegistry registry = REGISTRIES.get(identifier);

			if (registry != null) {
				SerializableRegistryMappings mappings = registry.getMappings();

				try {
					mappings.read(buffer, SyncedRegistriesPacketSerializer::deserialize, source);
				} catch (RegistryMappingException e) {
					throw new RegistryMappingException("Invalid registry mappings for " + identifier, e);
				}
			} else {
				RegistriesImpl.LOGGER.info("Skipping mappings for missing registry {}", identifier);
			}
		}
	}

	public static void serialize(PacketBuffer buffer, Object2IntMap<NamespacedIdentifier> mappings) throws IOException {
		buffer.writeVarInt(mappings.size());

		for (Object2IntMap.Entry<NamespacedIdentifier> mapping : mappings.object2IntEntrySet()) {
			NamespacedIdentifier identifier = mapping.getKey();
			int id = mapping.getIntValue();

			buffer.writeNamespacedIdentifier(identifier);
			buffer.writeVarInt(id);
		}
	}

	public static void deserialize(PacketBuffer buffer, Object2IntMap<NamespacedIdentifier> mappings, RegistryMappingSource source) throws IOException, RegistryMappingException {
		int size = buffer.readVarInt();

		for (int i = 0; i < size; i++) {
			NamespacedIdentifier identifier = buffer.readNamespacedIdentifier();
			int id = buffer.readVarInt();

			mappings.put(identifier, id);
		}
	}
}
