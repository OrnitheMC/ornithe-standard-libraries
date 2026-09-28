package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.osl.resource.loader.impl.world.WorldStorageExtensionImpl;

@Mixin(WorldStorage.class)
public interface WorldStorageMixin extends WorldStorageExtensionImpl {

	@Override
	default File getDirectory() {
		throw new UnsupportedOperationException();
	}
}
