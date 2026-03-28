package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Unique
	private SimpleReloadableResourceManager resourceManager;

	@Inject(
		method = "init",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/Minecraft;textureManager:Lnet/minecraft/client/render/texture/TextureManager;",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$initResourceManager(CallbackInfo ci) {
		this.resourceManager = SimpleReloadableResourceManager.client();

		this.resourceManager.init();
		this.resourceManager.partialReload();
	}
}
