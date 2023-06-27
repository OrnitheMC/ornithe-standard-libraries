package net.ornithemc.osl.branding.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.gui.overlay.DebugOverlay;

import net.ornithemc.osl.branding.impl.BrandingPatch;

@Mixin(DebugOverlay.class)
public class DebugOverlayMixin {

	@Redirect(
		method = "getGameInfo",
		at = @At(
			value = "INVOKE",
			ordinal = 1,
			target = "Lnet/minecraft/client/ClientBrandRetriever;getClientModName()Ljava/lang/String;"
		)
	)
	private String osl$branding$modifyVersionString() {
		return BrandingPatch.apply(ClientBrandRetriever.getClientModName());
	}
}
