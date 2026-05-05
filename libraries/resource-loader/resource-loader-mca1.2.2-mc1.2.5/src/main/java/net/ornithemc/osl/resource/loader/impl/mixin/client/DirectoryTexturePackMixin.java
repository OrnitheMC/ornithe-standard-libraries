package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.InputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resource.pack.BuiltInTexturePack;
import net.minecraft.client.resource.pack.DirectoryTexturePack;

// directory packs were added in 12w08a
@Mixin(DirectoryTexturePack.class)
public class DirectoryTexturePackMixin {

	@Inject(
		method = "getResource",
		cancellable = true,
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Class;getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;"
		)
	)
	private void osl$resource_loader$getResource(String path, CallbackInfoReturnable<InputStream> cir) {
		// the call to Class::getResourceAsStream is used as a fallback
		// in case custom texture packs do not contain a resource
		// we handle this by layering multiple packs
		if (!((Object) this instanceof BuiltInTexturePack)) {
			cir.setReturnValue(null);
		}
	}
}
