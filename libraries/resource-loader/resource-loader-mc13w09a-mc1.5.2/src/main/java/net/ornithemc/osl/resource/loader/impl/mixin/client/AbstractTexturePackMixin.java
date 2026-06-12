package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.resource.pack.AbstractTexturePack;
import net.minecraft.client.resource.pack.TexturePack;

@Mixin(AbstractTexturePack.class)
public class AbstractTexturePackMixin {

	@ModifyVariable(
		method = "<init>",
		argsOnly = true,
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/resource/pack/AbstractTexturePack;defaultTextures:Lnet/minecraft/client/resource/pack/TexturePack;"
		)
	)
	private TexturePack osl$resource_loader$removeFallbackTextures(TexturePack fallbackTextures) {
		// the fallback texture pack is used in case custom
		// texture packs do not contain a resource
		// we handle this by layering multiple packs
		return null;
	}
}
