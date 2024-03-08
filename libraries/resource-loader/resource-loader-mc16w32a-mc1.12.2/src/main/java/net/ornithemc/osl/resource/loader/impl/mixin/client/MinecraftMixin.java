package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.pack.ResourcePack;

import net.ornithemc.osl.resource.loader.api.ModResourcePack;
import net.ornithemc.osl.resource.loader.api.ResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.impl.BuiltInModResourcePack;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow @Final
	private List<ResourcePack> defaultResourcePacks;

	@Inject(
		method = "init",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Minecraft;reloadResources()V"
		)
	)
	private void osl$resource_loader$addDefaultResourcePacks(CallbackInfo ci) {
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			if ("builtin".equals(mod.getMetadata().getType())) {
				continue;
			}

			osl$resource_loader$addDefaultResourcePack(new BuiltInModResourcePack(mod));
		}

		ResourceLoaderEvents.ADD_DEFAULT_RESOURCE_PACKS.invoker().accept(this::osl$resource_loader$addDefaultResourcePack);
	}

	@Inject(
		method = "reloadResources",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$resource_loader$startResourceReload(CallbackInfo ci) {
		ResourceLoaderEvents.START_RESOURCE_RELOAD.invoker().run();
	}

	@Inject(
		method = "reloadResources",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$endResourceReload(CallbackInfo ci) {
		ResourceLoaderEvents.END_RESOURCE_RELOAD.invoker().run();
	}

	private void osl$resource_loader$addDefaultResourcePack(ModResourcePack pack) {
		if (ResourceLoader.addDefaultModResourcePack(pack)) {
			defaultResourcePacks.add(pack);
		}
	}
}
