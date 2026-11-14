package net.ornithemc.osl.resource.loader.impl.nbt;

public interface NbtListExtensionImpl {

	// this method was added in 13w36a so we hack it in
	// in 1.6 cause I don't wanna split this module in two
	default String getString(int index) {
		throw new AbstractMethodError();
	}
}
