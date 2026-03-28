package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.resource.pack.AbstractTexturePack;

@Mixin(AbstractTexturePack.class)
public interface AbstractTexturePackAccess {

	@Accessor("file")
	File accessFile();

}
