package net.ornithemc.osl.resource.loader.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.manager.ReloadableResourceManager;
import net.minecraft.resource.pack.repository.PackRepository;
import net.minecraft.resource.pack.repository.UnopenedPack;
import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.resource.loader.impl.adapter.ResourceManagerAdapter;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourcePackRepositoryAdapter;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Shadow @Final @Mutable
	private PackRepository<UnopenedPack> packRepository;
	@Shadow @Final @Mutable
	private ReloadableResourceManager resourceManager;

	@Unique
	private SimpleResourcePackRepository actualPackRepository;
	@Unique
	private SimpleReloadableResourceManager actualResourceManager;

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/server/MinecraftServer;packRepository:Lnet/minecraft/resource/pack/repository/PackRepository;",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$setPackRepository(CallbackInfo ci) {
		this.actualPackRepository = SimpleResourcePackRepository.server();
		this.packRepository = new ResourcePackRepositoryAdapter<>(this.packRepository.factory, this.actualPackRepository);
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/server/MinecraftServer;resourceManager:Lnet/minecraft/resource/manager/ReloadableResourceManager;",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$setResourceManager(CallbackInfo ci) {
		this.actualResourceManager = SimpleReloadableResourceManager.server();
		this.resourceManager = new ResourceManagerAdapter(this.actualResourceManager);
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL" // after last resource reloader is registered
		)
	)
	private void osl$resource_loader$initResourceManager(CallbackInfo ci) {
		this.actualResourceManager.init();
	}

	@Inject(
		method = "loadDataPacks",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resource/pack/repository/PackRepository;addSource(Lnet/minecraft/resource/pack/repository/RepositorySource;)V",
			ordinal = 0,
			shift = Shift.AFTER // after built-in Vanilla resources
		)
	)
	private void osl$resource_loader$registerModResources(CallbackInfo ci) {
		this.actualPackRepository.addSource(new BundledModResourcePacks());
	}

	@Inject(
		method = "loadDataPacks",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resource/pack/repository/PackRepository;addSource(Lnet/minecraft/resource/pack/repository/RepositorySource;)V",
			ordinal = 1,
			shift = Shift.AFTER // after directory pack source
		)
	)
	private void osl$resource_loader$initResourcePackRepository(CallbackInfo ci) {
		this.actualPackRepository.init();
	}
}
