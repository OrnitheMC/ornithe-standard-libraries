package net.ornithemc.osl.branding.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.screen.TitleScreen;

import static net.ornithemc.osl.branding.impl.BrandingPatch.versionType;;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

	@ModifyArg(
		method = "render(IIF)V",
		at = @At(
			value = "INVOKE",
			ordinal = 0,
			target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"
		)
	)
	public String osl$branding$modifyVersionString(String gameInfo) {
		if (versionType == null || "release".equals(versionType)) {
			return gameInfo;
		}

		return gameInfo + "/" + versionType;
	}
}
