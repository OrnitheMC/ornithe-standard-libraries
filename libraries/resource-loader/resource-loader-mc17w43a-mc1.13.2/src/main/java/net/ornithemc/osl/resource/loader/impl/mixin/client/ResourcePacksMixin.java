package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.resource.pack.ResourcePacks;
import net.minecraft.resource.pack.repository.UnopenedPack;

import net.ornithemc.osl.resource.loader.api.ModPack;
import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.impl.BuiltInModPack;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

@Mixin(ResourcePacks.class)
public class ResourcePacksMixin {

	@Inject(
		method = "loadPacks",
		at = @At(
			value = "TAIL"
		)
	)
	private <T extends UnopenedPack> void osl$resource_loader$addDefaultResourcePacks(Map<String, T> packs, UnopenedPack.Factory<T> factory, CallbackInfo ci) {
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			if ("builtin".equals(mod.getMetadata().getType())) {
				continue;
			}

			osl$resource_loader$addDefaultResourcePack(packs, factory, new BuiltInModPack(mod));
		}

		ClientResourceLoaderEvents.ADD_DEFAULT_RESOURCE_PACKS.invoker().accept(pack -> osl$resource_loader$addDefaultResourcePack(packs, factory, pack));
	}

	private <T extends UnopenedPack> void osl$resource_loader$addDefaultResourcePack(Map<String, T> packs, UnopenedPack.Factory<T> factory, ModPack pack) {
		if (ResourceLoader.addDefaultModPack(pack)) {
			packs.put(pack.getName(), UnopenedPack.create(pack.getName(), true, () -> pack, factory, UnopenedPack.Position.BOTTOM));
		}
	}
}
