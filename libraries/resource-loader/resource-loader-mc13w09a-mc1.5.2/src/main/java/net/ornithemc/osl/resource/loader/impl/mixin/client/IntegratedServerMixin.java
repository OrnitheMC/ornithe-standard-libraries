package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.osl.resource.loader.impl.mixin.common.MinecraftServerMixin;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin extends MinecraftServerMixin {

	@Inject(
		method = "loadWorld",
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/world/storage/WorldStorageSource;get(Ljava/lang/String;Z)Lnet/minecraft/world/storage/WorldStorage;"
			)
		),
		at = @At(
			// inject before the for-loop for ServerWorld instancing
			value = "FIELD",
			target = "Lnet/minecraft/server/integrated/IntegratedServer;worlds:[Lnet/minecraft/server/world/ServerWorld;",
			ordinal = 0,
			shift = Shift.BY,
			// it's extreme but this ensures that the injector is only invoked once
			by = -5
		)
	)
	private void osl$resource_loader$loadDataPacks(CallbackInfo ci, @Local WorldStorage storage) {
		this.onLoadWorldBeforeCreateServerWorlds(storage, storage.loadData());
	}

	@Inject(
		method = "loadWorld",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/integrated/IntegratedServer;prepareWorlds()V"
		)
	)
	private void osl$resource_loader$saveDataPacks(CallbackInfo ci) {
		this.saveDataPacks();
	}
}
