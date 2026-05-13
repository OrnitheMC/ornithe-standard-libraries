package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.manager.ReloadableResourceManager;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.client.resource.pack.BuiltInResourcePack;
import net.minecraft.client.resource.pack.ResourcePack;

import net.ornithemc.osl.resource.loader.impl.adapter.ResourceManagerAdapter;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourcePackLists;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow @Final
	private ResourceMetadataSerializerRegistry resourceMetadataSerializerRegistry;
	@Shadow @Final
	private List<ResourcePack> defaultResourcePacks;
	@Shadow @Final
	private BuiltInResourcePack defaultResourcePack;
	@Shadow
	private ReloadableResourceManager resourceManager;

	@Unique
	private SimpleReloadableResourceManager actualResourceManager;

	@Inject(
		method = "init",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/Minecraft;resourceManager:Lnet/minecraft/client/resource/manager/ReloadableResourceManager;",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$setResourceManager(CallbackInfo ci) {
		this.actualResourceManager = SimpleReloadableResourceManager.client();
		this.resourceManager = new ResourceManagerAdapter(this.resourceMetadataSerializerRegistry, this.actualResourceManager);

		this.actualResourceManager.reset();
	}

	@Inject(
		method = "init",
		at = @At(
			value = "NEW",
			target = "net/minecraft/client/gui/GameGui" // after all Vanilla reload listeners
		)
	)
	private void osl$resource_loader$initResourceManager(CallbackInfo ci) {
		this.actualResourceManager.init();
		this.actualResourceManager.partialReload();
	}

	@Inject(
		method = "reloadResources",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/manager/ReloadableResourceManager;reload(Ljava/util/List;)V"
		)
	)
	private void osl$resource_loader$fixSelection(CallbackInfo ci, @Local List<ResourcePack> selection) {
		// ResourcePacks.getSelectedPacks only contains directory or zip packs
		// The default pack and server pack are added in the target method, so
		// we must add any other required packs manually as well

		ResourcePackLists.fixSelection(selection, false);
	}
}
