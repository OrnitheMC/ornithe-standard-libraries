package net.ornithemc.osl.config.impl.mixin.common;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import net.ornithemc.osl.config.impl.interfaces.mixin.INbtList;

@Mixin(NbtList.class)
public class NbtListMixin implements INbtList {

	@Shadow @Final
	private List<NbtElement> elements;

	@Override
	public NbtElement osl$config$get(int index) {
		return elements.get(index);
	}
}
