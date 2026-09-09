package net.ornithemc.osl.entities.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entities.SpawnEggData;

@Mixin(SpawnEggData.class)
public interface SpawnEggDataAccessOld {

	@Mutable
	@Accessor("id")
	void setId(int id);

}
