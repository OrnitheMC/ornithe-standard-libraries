package net.ornithemc.osl.registries.impl.registry.sync;

import java.io.IOException;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.registries.api.registry.Registry;
import net.ornithemc.osl.registries.api.registry.ResourceKey;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

public class SerializableRegistryMappings implements RegistryMappings {

	public static SerializableRegistryMappings of(Registry<?> registry) {
		return new SerializableRegistryMappings(registry);
	}

	final Registry<Object> registry;

	final Object2IntMap<NamespacedIdentifier> mappings = new Object2IntOpenHashMap<>();
	final Object2IntMap<NamespacedIdentifier> unmappings = new Object2IntOpenHashMap<>();

	final Int2IntMap idMappings = new Int2IntOpenHashMap();
	final Int2IntMap idUnmappings = new Int2IntOpenHashMap();

	@SuppressWarnings("unchecked")
	private SerializableRegistryMappings(Registry<?> registry) {
		this.registry = (Registry<Object>) registry;
	}

	@Override
	public int remap(ResourceKey<?> key) {
		return this.mappings.getOrDefault(key.identifier(), -1);
	}

	@Override
	public int remap(int id) {
		return this.idMappings.getOrDefault(id, -1);
	}

	@Override
	public int unmap(ResourceKey<?> key) {
		return this.unmappings.getOrDefault(key.identifier(), -1);
	}

	@Override
	public int unmap(int id) {
		return this.idUnmappings.getOrDefault(id, -1);
	}

	public <M> void read(M medium, Deserializer<M> deserializer, RegistryMappingSource source) throws IOException, RegistryMappingException {
		deserializer.deserialize(medium, this.mappings, source);
	}

	public <M> void write(M medium, Serializer<M> serializer) throws IOException {
		serializer.serialize(medium, this.mappings);
	}

	public void reset() {
		this.mappings.clear();
		this.unmappings.clear();

		this.idMappings.clear();
		this.idUnmappings.clear();

		for (Object value : this.registry) {
			NamespacedIdentifier identifier = this.registry.getIdentifier(value);
			int id = this.registry.getId(value);

			this.mappings.put(identifier, id);
			this.unmappings.put(identifier, id);

			this.idMappings.put(id, id);
			this.idUnmappings.put(id, id);
		}
	}

	public void build(RegistryMappingSource source) throws RegistryMappingException {
		this.idMappings.clear();
		this.idUnmappings.clear();

		for (NamespacedIdentifier identifier : this.mappings.keySet()) {
			int newId = this.mappings.getInt(identifier);
			int oldId = this.unmappings.getInt(identifier);

			if (oldId >= 0) {
				this.idMappings.put(oldId, newId);
			} else if (source == RegistryMappingSource.REMOTE_SERVER) {
				throw new RegistryMappingException("received mapping for unknown entry " + identifier);
			}
		}

		for (NamespacedIdentifier identifier : this.unmappings.keySet()) {
			int oldId = this.unmappings.getInt(identifier);
			int newId = this.mappings.getInt(identifier);

			if (newId >= 0) {
				this.idUnmappings.put(newId, oldId);
			} else if (source == RegistryMappingSource.CLIENT) {
				throw new RegistryMappingException("missing mapping for required entry " + identifier);
			}
		}
	}

	@FunctionalInterface
	public interface Serializer<M> {

		void serialize(M medium, Object2IntMap<NamespacedIdentifier> mappings) throws IOException;

	}

	@FunctionalInterface
	public interface Deserializer<M> {

		void deserialize(M Medium, Object2IntMap<NamespacedIdentifier> mappings, RegistryMappingSource source) throws IOException, RegistryMappingException;

	}
}
