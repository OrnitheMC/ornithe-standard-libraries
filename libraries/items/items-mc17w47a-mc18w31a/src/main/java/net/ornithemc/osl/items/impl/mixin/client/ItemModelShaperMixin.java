package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import net.minecraft.client.render.item.ItemModelShaper;
import net.minecraft.client.resource.ModelIdentifier;
import net.minecraft.client.resource.model.BakedModel;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.Int2ObjectMapMapper;

@Mixin(ItemModelShaper.class)
public class ItemModelShaperMixin {

	@Shadow @Final
	private Int2ObjectMap<ModelIdentifier> models;
	@Shadow @Final
	private Int2ObjectMap<BakedModel> modelCache;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$items$registerModelRegistryMapper(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("item_model_location"), Int2ObjectMapMapper.of(this.models));
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("item_model"), Int2ObjectMapMapper.of(this.modelCache));
	}
}
