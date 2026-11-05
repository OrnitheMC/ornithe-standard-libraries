package net.ornithemc.osl.registries.impl.mixin.common;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.storage.AlphaWorldStorage;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.registries.impl.access.WorldStorageAccess;

@Conditional(minecraftVersion = @Version(">=b1.3"))
@Mixin(AlphaWorldStorage.class)
public class AlphaWorldStorageMixin implements WorldStorageAccess {

	@Shadow
	private File getDirectory() { return null; }

	@Override
	public File osl$registries$getDirectory() {
		return this.getDirectory();
	}
}
