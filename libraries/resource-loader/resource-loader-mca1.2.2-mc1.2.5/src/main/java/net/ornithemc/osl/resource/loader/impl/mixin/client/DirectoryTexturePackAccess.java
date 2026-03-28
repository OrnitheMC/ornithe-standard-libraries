package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.resource.pack.DirectoryTexturePack;

// directory packs were added in 12w08a
@Mixin(DirectoryTexturePack.class)
public interface DirectoryTexturePackAccess {

	@Accessor("file")
	File accessFile();

}
