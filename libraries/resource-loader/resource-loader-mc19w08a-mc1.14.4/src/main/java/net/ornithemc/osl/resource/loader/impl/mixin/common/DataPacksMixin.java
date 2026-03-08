package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.IOException;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.resource.pack.BuiltInPack;
import net.minecraft.resource.pack.PackType;
import net.minecraft.resource.pack.metadata.PackMetadataSection;
import net.minecraft.resource.pack.repository.UnopenedPack;
import net.minecraft.server.resource.pack.DataPacks;

import net.ornithemc.osl.resource.loader.api.ModPack;
import net.ornithemc.osl.resource.loader.api.server.ServerResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.impl.BuiltInModPack;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

@Mixin(DataPacks.class)
public class DataPacksMixin {

	@Shadow
	private BuiltInPack defaultPack;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$detetectPackFormat(CallbackInfo ci) {
		try {
			PackMetadataSection metadata = defaultPack.getMetadataSection(PackMetadataSection.SERIALIZER);
			int format = metadata.getFormat();

			ResourceLoader.setDataPackFormat(format);
		} catch (IOException e) {
			ResourceLoader.LOGGER.info("unable to parse data pack format from default data pack", e);
		}
	}

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
