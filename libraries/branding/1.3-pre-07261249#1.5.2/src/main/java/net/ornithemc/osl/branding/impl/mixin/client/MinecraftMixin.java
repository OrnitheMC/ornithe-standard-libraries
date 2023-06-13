package net.ornithemc.osl.branding.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.ornithemc.osl.branding.impl.BrandingPatch;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(
		method = "main",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$branding$parseVersionType(String[] args, CallbackInfo ci) {
		for (int i = 0; i < args.length - 1; i++) {
			if ("-versionType".equals(args[i]) || "--versionType".equals(args[i])) {
				BrandingPatch.versionType = args[i + 1];
				break;
			}
		}
	}
}
