package net.ornithemc.osl.items.impl.item;

import java.util.HashMap;
import java.util.Map;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

public class ItemModelCacheMapper implements IdMapper {

	public static ItemModelCacheMapper of(Map<Integer, ?> registry) {
		return new ItemModelCacheMapper(registry);
	}

	private static final int ID_SHIFT = 16;
	private static final int METADATA_MASK = (1 << ID_SHIFT) - 1;

	private final Map<Integer, Object> modelCache;

	private boolean applied;

	@SuppressWarnings("unchecked")
	private ItemModelCacheMapper(Map<Integer, ?> registry) {
		this.modelCache = (Map<Integer, Object>) registry;
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.mapModelRegistry(mappings::remap);

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.mapModelRegistry(mappings::unmap);
		}

		this.applied = false;
	}

	private void mapModelRegistry(Int2IntFunction mapper) {
		// we can't store a backup since this map will be
		// modified every time resource packs are reloaded
		Map<Integer, Object> modelCache = new HashMap<>();
		modelCache.putAll(this.modelCache);

		this.modelCache.clear();

		for (Map.Entry<Integer, Object> e : modelCache.entrySet()) {
			Object value = e.getValue();
			int index = e.getKey();
			int oldId = index >> ID_SHIFT;
			int metadata = index & METADATA_MASK;
			int newId = mapper.applyAsInt(oldId);

			if (newId >= 0) {
				this.modelCache.put(newId << ID_SHIFT | metadata, value);
			}
		}
	}
}
