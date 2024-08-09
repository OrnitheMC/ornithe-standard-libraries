package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.resource.pack.PackType;
import net.minecraft.resource.pack.repository.UnopenedPack;
import net.minecraft.server.resource.pack.DataPacks;

import net.ornithemc.osl.resource.loader.api.ModPack;
import net.ornithemc.osl.resource.loader.api.server.ServerResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.impl.BuiltInModPack;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

@Mixin(DataPacks.class)
public class DataPacksMixin {

	@Inject(
		method = "loadPacks",
		at = @At(
			value = "TAIL"
		)
	)
	private <T extends UnopenedPack> void osl$resource_loader$addDefaultDataPacks(Map<String, T> packs, UnopenedPack.Factory<T> factory, CallbackInfo ci) {
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			if ("builtin".equals(mod.getMetadata().getType())) {
				continue;
			}

			osl$resource_loader$addDefaultDataPack(packs, factory, new BuiltInModPack(mod, PackType.SERVER_DATA));
		}

		ServerResourceLoaderEvents.ADD_DEFAULT_DATA_PACKS.invoker().accept(pack -> osl$resource_loader$addDefaultDataPack(packs, factory, pack));
	}

	private <T extends UnopenedPack> void osl$resource_loader$addDefaultDataPack(Map<String, T> packs, UnopenedPack.Factory<T> factory, ModPack pack) {
		if (ResourceLoader.addDefaultModPack(pack)) {
			packs.put(pack.getName(), UnopenedPack.create(pack.getName(), true, () -> pack, factory, UnopenedPack.Position.BOTTOM));
		}
	}
}
