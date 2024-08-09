package net.ornithemc.osl.branding.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;

import net.ornithemc.osl.branding.api.BrandingContext;
import net.ornithemc.osl.branding.impl.BrandingPatchImpl;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

	@Inject(
		method = "render(IIF)V",
		at = @At(
			value = "INVOKE",
			ordinal = 0,
			target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"
		)
	)
	public void osl$branding$drawVersionString(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
		String gameVersion = BrandingPatchImpl.getGameVersion();
		String gameInfo = BrandingPatchImpl.apply(BrandingContext.TITLE_SCREEN, "Minecraft " + gameVersion);

		drawString(textRenderer, gameInfo, 2, 2, 0x505050);
	}
}
