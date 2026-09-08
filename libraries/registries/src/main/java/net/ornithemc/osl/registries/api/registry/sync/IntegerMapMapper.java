package net.ornithemc.osl.registries.api.registry.sync;

import java.util.HashMap;
import java.util.Map;

/**
 * An {@linkplain IdMapper} implementation for maps with {@code Integer} keys
 * ({@code Map<Integer, T>}) where IDs are used as keys to the map. The values
 * are moved from the old key (the old ID) to the new key (the new ID) within
 * the same map. {@code null} is used as the default or "empty" value.
 */
public class IntegerMapMapper implements IdMapper {

	public static IntegerMapMapper of(Map<Integer, ?> registry) {
		return new IntegerMapMapper(registry);
	}

	private final Map<Integer, Object> registry;
	private final Map<Integer, Object> backup;

	private boolean applied;

	@SuppressWarnings("unchecked")
	private IntegerMapMapper(Map<Integer, ?> registry) {
		this.registry = (Map<Integer, Object>) registry;
		this.backup = new HashMap<>(this.registry.size());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.backup.clear();
		this.backup.putAll(this.registry);

		this.registry.clear();

		for (Map.Entry<Integer, Object> e : this.backup.entrySet()) {
			Object value = e.getValue();
			int oldId = e.getKey();
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
