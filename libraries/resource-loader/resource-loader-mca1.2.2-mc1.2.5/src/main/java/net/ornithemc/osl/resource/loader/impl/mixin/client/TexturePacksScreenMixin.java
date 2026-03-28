package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.gui.screen.TexturePacksScreen;
import net.minecraft.client.resource.pack.TexturePack;
import net.minecraft.client.resource.pack.TexturePacks;

import net.ornithemc.osl.resource.loader.impl.access.TexturePacksAccess;

@Mixin(TexturePacksScreen.class)
public class TexturePacksScreenMixin {

	@Redirect(
		method = "render",
		require = 0, // b1.2-
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/resource/pack/TexturePacks;selected:Lnet/minecraft/client/resource/pack/TexturePack;"
		)
	)
	private TexturePack osl$resource_loader$getSelectedPack(TexturePacks texturePacks) {
		return ((TexturePacksAccess) texturePacks).osl$resource_loader$getActuallySelected();
	}
}
