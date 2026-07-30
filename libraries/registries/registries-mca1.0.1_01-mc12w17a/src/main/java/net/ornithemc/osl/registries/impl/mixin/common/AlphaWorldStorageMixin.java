package net.ornithemc.osl.registries.impl.mixin.common;

import java.io.File;
import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.storage.AlphaWorldStorage;

import net.ornithemc.osl.registries.impl.access.WorldStorageAccess;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingStorage;

@Mixin(AlphaWorldStorage.class)
public class AlphaWorldStorageMixin implements WorldStorageAccess {

	@Shadow
	private File getDirectory() { return null; }

	@Override
	public void osl$registries$loadRegistryMappings() throws IOException {
		RegistryMappingStorage.loadRegistryMappings(this.getDirectory());
	}

	@Override
	public void osl$registries$saveRegistryMappings() throws IOException {
		RegistryMappingStorage.saveRegistryMappings(this.getDirectory());
	}
}
