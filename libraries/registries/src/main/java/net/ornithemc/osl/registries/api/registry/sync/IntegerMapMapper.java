package net.ornithemc.osl.registries.api.registry.sync;

import java.util.HashMap;
import java.util.Map;

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
