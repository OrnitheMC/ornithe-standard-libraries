package net.ornithemc.osl.entities.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.Entities.SpawnEggData;

@Mixin(SpawnEggData.class)
public interface SpawnEggDataAccessNew {

	@Invoker("<init>")
	static SpawnEggData of(String legacyKey, int baseColor, int spotsColor) {
		throw new UnsupportedOperationException();
	}
}
