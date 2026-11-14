package net.ornithemc.osl.resource.loader.impl.nbt;

import net.minecraft.nbt.NbtList;

public interface NbtCompoundExtensionImpl {

	// this method changed signature in 13w36a so we hack it in
	// in 1.6 cause I don't wanna split this module in two
	default NbtList getList(String key, int type) {
		throw new AbstractMethodError();
	}
}
