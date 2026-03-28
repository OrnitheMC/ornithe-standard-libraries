package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.resource.loader.impl.ClientResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Unique
	private SimpleResourcePackRepository packRepository;

	@Inject(
		method = "init",
		at = @At(
			value = "NEW",
			target = "net/minecraft/client/render/texture/TextureManager"
		)
	)
	private void osl$resource_loader$initPackRepository(CallbackInfo ci) {
		this.packRepository = SimpleResourcePackRepository.client();

		this.packRepository.addSource(new ClientResourcePacks());
		this.packRepository.addSource(new BundledModResourcePacks());
//		this.packRepository.addSource(new DirectoryResourcePackSource()); // TODO

		this.packRepository.init();
		this.packRepository.reload();
	}
}
