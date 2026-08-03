package net.ornithemc.osl.entities.impl.entity;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.entity.Entities.SpawnEggData;

import net.ornithemc.osl.entities.impl.mixin.common.SpawnEggDataAccessOld;
import net.ornithemc.osl.registries.api.registry.sync.IdMapper;
import net.ornithemc.osl.registries.api.registry.sync.RegistryMappings;

public class SpawnEggDataMapper implements IdMapper {

	public static SpawnEggDataMapper of(Map<Integer, SpawnEggData> spawnEggData) {
		return new SpawnEggDataMapper(spawnEggData);
	}

	private final Map<Integer, SpawnEggData> spawnEggData;
	private final Set<SpawnEggData> missing;

	private boolean applied;

	private SpawnEggDataMapper(Map<Integer, SpawnEggData> spawnEggData) {
		this.spawnEggData = spawnEggData;
		this.missing = Collections.newSetFromMap(new IdentityHashMap<>());
	}

	@Override
	public void apply(RegistryMappings mappings) {
		this.missing.clear();
		this.fixSpawnEggData(mappings::remap, true);

		this.applied = true;
	}

	@Override
	public void undo(RegistryMappings mappings) {
		if (this.applied) {
			this.fixSpawnEggData(mappings::unmap, false);
			this.missing.clear();
		}

		this.applied = false;
	}

	private void fixSpawnEggData(Int2IntFunction mapper, boolean storeMissing) {
		for (SpawnEggData spawnEgg : this.spawnEggData.values()) {
			if (this.missing.contains(spawnEgg)) {
				continue;
			}

			SpawnEggDataAccessOld spawnEggAccess = (SpawnEggDataAccessOld) spawnEgg;

			int oldId = spawnEgg.id;
			int newId = mapper.applyAsInt(oldId);

			if (newId >= 0) {
				spawnEggAccess.setId(newId);
			} else if (storeMissing) {
				this.missing.add(spawnEgg);
			}
		}
	}
}
