package net.ornithemc.osl.branding.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.screen.TitleScreen;

import net.ornithemc.osl.branding.BrandingPatch;

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
	public String osl$branding$modifyGameInfo(String gameInfo) {
		return BrandingPatch.versionType.equals("release") ? gameInfo : gameInfo + "/" + BrandingPatch.versionType;
	}
}
