package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.resource.pack.ZippedTexturePack;

@Mixin(ZippedTexturePack.class)
public interface ZippedTexturePackAccess {

	@Accessor("file")
	File accessFile();

}
