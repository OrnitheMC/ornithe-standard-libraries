package net.ornithemc.osl.registries.api.registry.sync;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * An {@linkplain IdMapper} implementation for fastutil's
 * {@linkplain Int2ObjectMap} where IDs are used as keys to the map. The values
 * are moved from the old key (the old ID) to the new key (the new ID) within
 * the same map. {@code null} is used as the default or "empty" value.
 */
public class Int2ObjectMapMapper implements IdMapper {

	public static Int2ObjectMapMapper of(Int2ObjectMap<?> registry) {
		return new Int2ObjectMapMapper(registry);
	}

	private final Int2ObjectMap<Object> registry;
	private final Int2ObjectMap<Object> backup;

	private boolean applied;

	@SuppressWarnings("unchecked")
	private Int2ObjectMapMapper(Int2ObjectMap<?> registry) {
		this.registry = (Int2ObjectMap<Object>) registry;
		this.backup = new Int2ObjectOpenHashMap<>(registry.size());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.backup.clear();
		this.backup.putAll(this.registry);

		this.registry.clear();

		for (Int2ObjectMap.Entry<Object> e : this.backup.int2ObjectEntrySet()) {
			Object value = e.getValue();
			int oldId = e.getIntKey();
			int newId = mappings.remap(oldId);

			if (newId >= 0) {
				this.registry.put(newId, value);
			}
		}

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.registry.clear();
			this.registry.putAll(this.backup);
		}

		this.applied = false;
	}
}
