package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.world.WorldRenderer;
import net.minecraft.client.resource.pack.TexturePack;
import net.minecraft.client.resource.pack.TexturePacks;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.resource.loader.impl.access.TexturePacksAccess;

@Mixin(targets = "net/minecraft/client/gui/screen/TexturePacksScreen$TexturePackList")
public class TexturePackListMixin {

	@Conditional(minecraftVersion = @Version(">=13w04a"))
	@Redirect(
		method = "entryClicked",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/world/WorldRenderer;reload()V"
		)
	)
	private void osl$resource_loader$cancelWorldRendererReload(WorldRenderer worldRenderer) {
		// WorldRenderer reload is handled in Minecraft.reloadResources()
	}

	@Redirect(
		method = "isEntrySelected",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/pack/TexturePacks;getSelected()Lnet/minecraft/client/resource/pack/TexturePack;"
		)
	)
	private TexturePack osl$resource_loader$getSelectedPack(TexturePacks texturePacks) {
		return ((TexturePacksAccess) texturePacks).osl$resource_loader$getActuallySelected();
	}
}
