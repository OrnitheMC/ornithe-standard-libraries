package net.ornithemc.osl.registries.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.stat.Stat;

@Mixin(Stat.class)
public interface StatAccess {

	@Mutable
	@Accessor("key")
	void setKey(String key);

}
