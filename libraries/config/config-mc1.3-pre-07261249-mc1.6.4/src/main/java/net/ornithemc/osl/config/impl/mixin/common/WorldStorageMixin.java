package net.ornithemc.osl.config.impl.mixin.common;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.osl.config.impl.interfaces.mixin.IWorldStorage;

@Mixin(WorldStorage.class)
public interface WorldStorageMixin extends IWorldStorage {

	@Override
	default File osl$config$getDir() {
		throw new RuntimeException("this world storage does not have a directory!");
	}
}
