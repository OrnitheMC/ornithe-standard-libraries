package net.ornithemc.osl.entities.impl;

import net.minecraft.entity.Entities.SpawnEggData;

public interface SpawnEggDataRegistry {

	void put(int id, String legacyId, int baseColor, int spotsColor);

	boolean contains(int id, String legacyId);

	SpawnEggData get(int id, String legacyId);

}
