package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.resource.loader.impl.world.WorldStorageExtensionImpl;

@Conditional(minecraftVersion = @Version("<=1.6.4"))
@Mixin(WorldStorage.class)
public interface WorldStorageMixin extends WorldStorageExtensionImpl {

	@Override
	default File getDirectory() {
		throw new UnsupportedOperationException();
	}
}
