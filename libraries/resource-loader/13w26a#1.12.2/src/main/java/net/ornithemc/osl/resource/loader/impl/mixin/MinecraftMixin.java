package net.ornithemc.osl.resource.loader.impl.mixin;

import java.util.List;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.ModContainer.BasicSourceType;
import org.quiltmc.loader.api.QuiltLoader;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.pack.ResourcePack;

import net.ornithemc.osl.resource.loader.impl.BuiltInModResourcePack;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow @Final private List<ResourcePack> defaultResourcePacks;

	@Inject(
		method = "init",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Minecraft;reloadResources()V"
		)
	)
	private void osl$resource_loader$addModResourcePacks(CallbackInfo ci) {
		for (ModContainer mod : QuiltLoader.getAllMods()) {
			if (mod.getSourceType() == BasicSourceType.BUILTIN) {
				continue;
			}

			defaultResourcePacks.add(new BuiltInModResourcePack(mod));
		}
	}
}
