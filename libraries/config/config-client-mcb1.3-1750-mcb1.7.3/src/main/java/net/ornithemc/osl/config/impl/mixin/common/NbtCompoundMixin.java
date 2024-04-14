package net.ornithemc.osl.config.impl.mixin.common;

import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import net.ornithemc.osl.config.impl.interfaces.mixin.INbtCompound;

@Mixin(NbtCompound.class)
public class NbtCompoundMixin implements INbtCompound {

	@Shadow
	private Map<String, NbtElement> elements;

	@Override
	public Set<String> osl$config$keySet() {
		return elements.keySet();
	}

	@Override
	public NbtElement osl$config$get(String key) {
		return elements.get(key);
	}
}
