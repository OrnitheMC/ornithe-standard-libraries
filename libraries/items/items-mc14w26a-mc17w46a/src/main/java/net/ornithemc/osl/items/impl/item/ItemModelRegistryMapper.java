package net.ornithemc.osl.items.impl.item;

import java.util.HashMap;
import java.util.Map;

import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

public class ItemModelRegistryMapper implements IdMapper {

	public static ItemModelRegistryMapper of(Map<Integer, ?> registry) {
		return new ItemModelRegistryMapper(registry);
	}

	private static final int ID_SHIFT = 16;
	private static final int METADATA_MASK = (1 << ID_SHIFT) - 1;

	private final Map<Integer, Object> registry;
	private final Map<Integer, Object> backup;

	private boolean applied;

	@SuppressWarnings("unchecked")
	private ItemModelRegistryMapper(Map<Integer, ?> registry) {
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
			int index = e.getKey();
			int oldId = index >> ID_SHIFT;
			int metadata = index & METADATA_MASK;
			int newId = mappings.remap(oldId);

			if (newId >= 0) {
				this.registry.put(newId << ID_SHIFT | metadata, value);
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
