package net.ornithemc.osl.registries.api.registry.sync;

import java.util.HashMap;
import java.util.Map;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

/**
 * An {@linkplain Int2ObjectMapMapper} implementation for dymamic maps, i.e maps
 * whose contents may change throughout a world session.
 * 
 * @see Int2ObjectMapMapper
 */
public class DynamicInt2ObjectMapMapper implements IdMapper {

	public static DynamicInt2ObjectMapMapper of(Int2ObjectMap<?> cache) {
		return new DynamicInt2ObjectMapMapper(cache);
	}

	private final Int2ObjectMap<Object> cache;

	private boolean applied;

	@SuppressWarnings("unchecked")
	private DynamicInt2ObjectMapMapper(Int2ObjectMap<?> cache) {
		this.cache = (Int2ObjectMap<Object>) cache;
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.mapCache(mappings::remap);

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.mapCache(mappings::unmap);
		}

		this.applied = false;
	}

	private void mapCache(Int2IntFunction mapper) {
		Map<Integer, Object> oldCache = new HashMap<>();
		oldCache.putAll(this.cache);

		this.cache.clear();

		for (Map.Entry<Integer, Object> e : oldCache.entrySet()) {
			Object value = e.getValue();
			int oldId = e.getKey();
			int newId = mapper.applyAsInt(oldId);

			if (newId >= 0) {
				this.cache.put(newId, value);
			}
		}
	}
}
