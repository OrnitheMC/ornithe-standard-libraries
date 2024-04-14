package net.ornithemc.osl.config.impl.interfaces.mixin;

import java.util.Set;

import net.minecraft.nbt.NbtElement;

public interface INbtCompound {

	Set<String> osl$config$keySet();

	NbtElement osl$config$get(String key);

}
