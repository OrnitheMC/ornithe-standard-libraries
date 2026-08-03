package net.ornithemc.osl.entities.impl;

import net.minecraft.entity.Entity;

@FunctionalInterface
public interface SpawnEggDataRegistry {

	void register(Class<? extends Entity> type, int baseColor, int spotsColor);

}
