package net.ornithemc.osl.branding.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.overlay.DebugOverlay;

import net.ornithemc.osl.branding.api.BrandingContext;
import net.ornithemc.osl.branding.impl.BrandingPatchImpl;
import net.ornithemc.osl.branding.impl.Constants;

@Mixin(DebugOverlay.class)
public class DebugOverlayMixin {

	@Redirect(
		method = "getGameInfo",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Minecraft;m_5774562()Ljava/lang/String;"
		)
	)
	private String osl$branding$modifyVersionType(Minecraft minecraft) {
		return Constants.RELEASE; // we register a vanilla modifier ourselves
	}

	@Redirect(
		method = "getGameInfo",
		at = @At(
			value = "INVOKE",
			ordinal = 1,
			target = "Lnet/minecraft/client/ClientBrandRetriever;getClientModName()Ljava/lang/String;"
		)
	)
	private String osl$branding$modifyVersionString() {
		return BrandingPatchImpl.apply(BrandingContext.DEBUG_OVERLAY, ClientBrandRetriever.getClientModName());
	}
}
