package net.ornithemc.osl.registries.impl.registry.sync;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.stat.Stat;

import net.ornithemc.osl.registries.api.registry.sync.ArrayMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;
import net.ornithemc.osl.registries.impl.mixin.common.StatAccess;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

public class StatsMapper extends ArrayMapper {

	public static StatsMapper of(Map<String, Stat> stats, Stat[] registry) {
		return new StatsMapper(stats, registry);
	}

	private final Map<String, Stat> stats;
	private final Map<String, Stat> missing;

	private StatsMapper(Map<String, Stat> stats, Stat[] registry) {
		super(registry);

		this.stats = stats;
		this.missing = new HashMap<>();
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.missing.clear();
		this.fixKeys(mappings::remap, true);

		super.apply(mappings);
	}

	@Override
	public void undo(RegistryMappings mappings) {
		super.undo(mappings);

		this.fixKeys(mappings::unmap, false);
		this.stats.putAll(this.missing);
	}

	private void fixKeys(Int2IntFunction mapper, boolean storeMissingStats) {
		Map<String, Stat> fixed = new HashMap<>();

		// some blocks/items share a single Stat!
		Arrays.stream(this.registry).distinct().forEach(value -> {
			if (value == null) {
				return;
			}

			Stat stat = (Stat) value;

			int i = stat.key.lastIndexOf('.');
			String base = stat.key.substring(0, i + 1);
			String id = stat.key.substring(i + 1);

			try {
				int oldId = Integer.parseInt(id);
				int newId = mapper.apply(oldId);

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
		});

		this.stats.putAll(fixed);
	}
}
