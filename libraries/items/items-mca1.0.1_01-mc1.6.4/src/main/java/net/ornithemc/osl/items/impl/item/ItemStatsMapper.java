package net.ornithemc.osl.items.impl.item;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.stat.ItemStat;
import net.minecraft.stat.Stat;

import net.ornithemc.osl.items.impl.mixin.common.ItemStatAccess;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

public class ItemStatsMapper implements IdMapper {

	public static ItemStatsMapper of(List<Stat> stats) {
		return new ItemStatsMapper(stats);
	}

	private final List<Stat> stats;
	private final Set<Stat> missing;

	private boolean applied;

	private ItemStatsMapper(List<Stat> stats) {
		this.stats = stats;
		this.missing = Collections.newSetFromMap(new IdentityHashMap<>());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.missing.clear();
		this.fixItemStats(mappings::remap, true);

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.fixItemStats(mappings::unmap, false);
			this.missing.clear();
		}

		this.applied = false;
	}

	private void fixItemStats(Int2IntFunction mapper, boolean storeMissing) {
		for (Stat stat : this.stats) {
			if (!(stat instanceof ItemStat)) {
				continue;
			}

			ItemStatAccess itemStat = (ItemStatAccess) stat;

			if (this.missing.contains(stat)) {
				continue;
			}

			int oldId = itemStat.accessItem();
			int newId = mapper.applyAsInt(oldId);

			if (newId >= 0) {
				itemStat.setItem(newId);
			} else if (storeMissing) {
				this.missing.add(stat);
			}
		}
	}
}
