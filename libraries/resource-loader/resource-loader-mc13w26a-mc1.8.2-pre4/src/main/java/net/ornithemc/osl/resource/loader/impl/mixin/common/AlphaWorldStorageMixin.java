package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.storage.AlphaWorldStorage;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.resource.loader.impl.world.WorldStorageExtensionImpl;

@Conditional(minecraftVersion = @Version("<=1.6.4"))
@Mixin(AlphaWorldStorage.class)
public class AlphaWorldStorageMixin implements WorldStorageExtensionImpl {

	@Shadow
	private File dir;

	@Override
	public File getDirectory() {
		return this.dir;
	}
}
