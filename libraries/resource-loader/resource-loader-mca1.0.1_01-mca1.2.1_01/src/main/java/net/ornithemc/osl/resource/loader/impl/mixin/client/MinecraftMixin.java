package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.resource.loader.impl.ClientResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Unique
	private SimpleResourcePackRepository packRepository;
	@Unique
	private SimpleReloadableResourceManager resourceManager;

	@Inject(
		method = "init",
		at = @At(
			value = "NEW",
			target = "net/minecraft/client/render/texture/TextureManager"
		)
	)
	private void osl$resource_loader$initPackRepository(CallbackInfo ci) {
		this.packRepository = SimpleResourcePackRepository.client();

		this.packRepository.reset();
		this.packRepository.setCallbacks(null, null);

		this.packRepository.addSource(new ClientResourcePacks());
		this.packRepository.addSource(new BundledModResourcePacks());
//		this.packRepository.addSource(new DirectoryResourcePackSource()); // TODO

		this.packRepository.init();
		this.packRepository.reload();
	}

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
