package net.ornithemc.osl.config.impl.mixin.common;

import java.io.File;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.storage.RegionWorldStorage;

import net.ornithemc.osl.config.impl.interfaces.mixin.IWorldStorage;

@Mixin(RegionWorldStorage.class)
public class RegionWorldStorageMixin implements IWorldStorage {

	@Shadow @Final private File dir;

	@Override
	public File osl$config$getDir() {
		return dir;
	}
}
