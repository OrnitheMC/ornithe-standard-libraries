package net.ornithemc.osl.registries.impl.registry.sync;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.stat.Stat;

import net.ornithemc.osl.registries.api.registry.sync.ArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArray;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;
import net.ornithemc.osl.registries.impl.mixin.common.StatAccess;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public class StatsMapper implements IdMapper {

	public static StatsMapper of(Map<String, Stat> stats, Supplier<Stat[]> getter, Consumer<Stat[]> setter) {
		return new StatsMapper(stats, DynamicArray.of(getter, setter));
	}

	public static StatsMapper of(Map<String, Stat> stats, DynamicArray<Stat> registry) {
		return new StatsMapper(stats, registry);
	}

	private final IdMapper registryMapper;

	private final DynamicArray<Stat> registry;
	private final Map<String, Stat> stats;
	private final Map<String, Stat> missing;

	private boolean applied;

	private StatsMapper(Map<String, Stat> stats, DynamicArray<Stat> registry) {
		this.registryMapper = ArrayMapper.of(registry);

		this.registry = registry;
		this.stats = stats;
		this.missing = new HashMap<>();
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.missing.clear();
		this.fixKeys(mappings::remap, true);

		this.registryMapper.apply(mappings);

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		this.registryMapper.undo(mappings);

		if (this.applied) {
			this.fixKeys(mappings::unmap, false);
			this.stats.putAll(this.missing);
		}

		this.applied = false;
	}

	private void fixKeys(Int2IntFunction mapper, boolean storeMissingStats) {
		// some blocks/items share a single Stat!
		Set<Stat> visited = Collections.newSetFromMap(new IdentityHashMap<>());
		Map<String, Stat> fixed = new HashMap<>();

		for (int index = 0; index < this.registry.length(); index++) {
			Stat stat = this.registry.get(index);

			if (stat == null || !visited.add(stat)) {
				return;
			}

			int i = stat.key.lastIndexOf('.');
			String base = stat.key.substring(0, i + 1);
			String id = stat.key.substring(i + 1);

			try {
				int oldId = Integer.parseInt(id);
				int newId = mapper.applyAsInt(oldId);

				if (oldId != newId) {
					this.stats.remove(stat.key);

					if (newId >= 0) {
						StatAccess statAccess = (StatAccess) stat;
						statAccess.setKey(base + newId);

						fixed.put(stat.key, stat);
					} else if (storeMissingStats) {
						this.missing.put(stat.key, stat);
					}
				}
			} catch (NumberFormatException e) {
				RegistriesImpl.LOGGER.warn("Stat key {} does not follow expected format of 'statType.<id>'", stat.key);
			}
		}

		this.stats.putAll(fixed);
	}
}
