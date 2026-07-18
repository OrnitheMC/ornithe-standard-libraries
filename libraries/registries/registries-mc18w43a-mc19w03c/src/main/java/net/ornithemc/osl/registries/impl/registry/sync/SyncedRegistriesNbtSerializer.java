package net.ornithemc.osl.registries.impl.registry.sync;

import java.io.IOException;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.nbt.NbtCompound;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.impl.Constants;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistry;

public final class SyncedRegistriesNbtSerializer {

	private static final Registry<SyncedRegistry> REGISTRIES = SyncedRegistriesImpl.SYNCED_REGISTRIES;

	public static void serialize(NbtCompound nbt) throws IOException {
		NbtCompound registriesNbt = new NbtCompound();

		for (SyncedRegistry registry : REGISTRIES) {
			NamespacedIdentifier identifier = registry.identifier();
			SerializableRegistryMappings mappings = registry.getMappings();

			NbtCompound mappingsNbt = new NbtCompound();

			mappings.write(mappingsNbt, SyncedRegistriesNbtSerializer::serialize);
			registriesNbt.put(identifier.toString(), mappingsNbt);
		}

		nbt.putInt(Constants.FORMAT_NBT_KEY, Constants.REGISTRY_MAPPINGS_FORMAT);
		nbt.put(Constants.REGISTRIES_NBT_KEY, registriesNbt);
	}

	public static void deserialize(NbtCompound nbt, RegistryMappingSource source) throws IOException, RegistryMappingException {
		int format = nbt.getInt(Constants.FORMAT_NBT_KEY);

		if (format > Constants.REGISTRY_MAPPINGS_FORMAT) {
			throw new IllegalStateException("cannot read registry mappings of newer format " + format);
		}

		NbtCompound registriesNbt = nbt.getCompound(Constants.REGISTRIES_NBT_KEY);

		for (String key : registriesNbt.getKeys()) {
			NbtCompound mappingsNbt = registriesNbt.getCompound(key);

			NamespacedIdentifier identifier = NamespacedIdentifiers.parse(key);
			SyncedRegistry registry = REGISTRIES.get(identifier);

			if (registry != null) {
				SerializableRegistryMappings mappings = registry.getMappings();

				try {
					mappings.read(mappingsNbt, SyncedRegistriesNbtSerializer::deserialize, source);
					mappings.build(source);
				} catch (RegistryMappingException e) {
					throw new RegistryMappingException("Invalid registry mappings for " + identifier, e);
				}
			} else {
				RegistriesImpl.LOGGER.info("Skipping mappings for missing registry {}", identifier);
			}
		}
	}

	public static void serialize(NbtCompound nbt, Object2IntMap<NamespacedIdentifier> mappings) throws IOException {
		for (Object2IntMap.Entry<NamespacedIdentifier> mapping : mappings.object2IntEntrySet()) {
			nbt.putInt(mapping.getKey().toString(), mapping.getIntValue());
		}
	}

	public static void deserialize(NbtCompound nbt, Object2IntMap<NamespacedIdentifier> mappings, RegistryMappingSource source) throws IOException, RegistryMappingException {
		for (String key : nbt.getKeys()) {
			mappings.put(NamespacedIdentifiers.parse(key), nbt.getInt(key));
		}
	}
}
