package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resource.pack.TexturePack;

import net.ornithemc.osl.resource.loader.api.ModTexturePack;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

@Mixin(TexturePack.class)
public class TexturePackMixin {

	private final Set<ModTexturePack> modTextures = new LinkedHashSet<>();

	@Inject(
		method = "<init>()V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$init(CallbackInfo ci) {
		if (!((TexturePack)(Object)this instanceof ModTexturePack)) {
			modTextures.addAll(ResourceLoader.getDefaultModResourcePacks());
		}
	}

	@Inject(
		method = "getResource(Ljava/lang/String;)Ljava/io/InputStream;",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$resource_loader$getResource(String path, CallbackInfoReturnable<InputStream> cir) {
		for (ModTexturePack textures : modTextures) {
			try {
				InputStream resource = textures.getResource(path);

				if (resource != null) {
					cir.setReturnValue(resource);
				}
			} catch (Exception e) {
			}
		}
	}
}
