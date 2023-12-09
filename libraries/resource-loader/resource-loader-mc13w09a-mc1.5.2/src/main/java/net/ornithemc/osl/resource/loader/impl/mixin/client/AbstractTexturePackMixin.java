package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.client.resource.pack.AbstractTexturePack;

import net.ornithemc.osl.resource.loader.api.ModTexturePack;
import net.ornithemc.osl.resource.loader.api.ResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.impl.BuiltInModTexturePack;

@Mixin(AbstractTexturePack.class)
public class AbstractTexturePackMixin {

	private final Set<ModTexturePack> modTextures = new LinkedHashSet<>();

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/pack/AbstractTexturePack;loadIcon()V"
		)
	)
	private void osl$resource_loader$init(CallbackInfo ci) {
		if (!(this instanceof ModTexturePack)) {
			for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
				if ("builtin".equals(mod.getMetadata().getType())) {
					continue;
				}

				this.modTextures.add(new BuiltInModTexturePack(mod));
			}

			ResourceLoaderEvents.ADD_DEFAULT_TEXTURE_PACKS.invoker().accept(this.modTextures::add);
		}
	}

	@Inject(
		method = "getResource(Ljava/lang/String;Z)Ljava/io/InputStream;",
		cancellable = true,
		at = @At(
			value = "FIELD",
			ordinal = 0,
			target = "Lnet/minecraft/client/resource/pack/AbstractTexturePack;defaultTextures:Lnet/minecraft/client/resource/pack/TexturePack;"
		)
	)
	private void osl$resource_loader$getResource(String path, boolean orDefault, CallbackInfoReturnable<InputStream> cir) {
		if (orDefault) {
			for (ModTexturePack textures : modTextures) {
				try {
					InputStream resource = textures.getResource(path, orDefault);

					if (resource != null) {
						cir.setReturnValue(resource);
					}
				} catch (Exception e) {
				}
			}
		}
	}

	@Inject(
		method = "getResource(Ljava/lang/String;Z)Ljava/io/InputStream;",
		cancellable = true,
		at = @At(
			value = "FIELD",
			ordinal = 0,
			target = "Lnet/minecraft/client/resource/pack/AbstractTexturePack;defaultTextures:Lnet/minecraft/client/resource/pack/TexturePack;"
		)
	)
	private void osl$resource_loader$hasResource(String path, boolean orDefault, CallbackInfoReturnable<Boolean> cir) {
		if (orDefault) {
			for (ModTexturePack textures : modTextures) {
				try {
					boolean hasResource = textures.hasResource(path, orDefault);

					if (hasResource) {
						cir.setReturnValue(hasResource);
					}
				} catch (Exception e) {
				}
			}
		}
	}
}
