package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.storage.AlphaWorldStorage;

import net.ornithemc.osl.resource.loader.impl.world.WorldStorageExtensionImpl;

@Mixin(AlphaWorldStorage.class)
public class AlphaWorldStorageMixin implements WorldStorageExtensionImpl {

	@Shadow
	private File dir;

	@Override
	public File getDirectory() {
		return this.dir;
	}
}
