package net.ornithemc.osl.registries.impl.mixin.server;

import java.io.File;
import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.storage.AnvilWorldStorage;
import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.osl.registries.impl.access.WorldStorageAccess;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixinNew {

	@Shadow
	private ServerWorld f_58488472;

	@WrapOperation(
		method = "loadWorld",
		at = @At(
			value = "NEW",
			target = "net/minecraft/world/storage/AnvilWorldStorage"
		)
	)
	private AnvilWorldStorage osl$registries$loadRegistryMappings(File file, String saveName, boolean createPlayersDir, Operation<AnvilWorldStorage> op) {
		AnvilWorldStorage storage = op.call(file, saveName, createPlayersDir);

		if (storage instanceof WorldStorageAccess) {
			try {
				((WorldStorageAccess) storage).osl$registries$loadRegistryMappings();
			} catch (IOException e) {
				throw new RuntimeException("Exception initializing level", e);
			}
		}

		return storage;
	}

	@Inject(
		method = "saveWorlds",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$registries$saveRegistryMappings(CallbackInfo ci) {
		ServerWorld overworld = this.f_58488472;
		WorldStorage storage = overworld.getStorage();

		if (storage instanceof WorldStorageAccess) {
			try {
				((WorldStorageAccess) storage).osl$registries$saveRegistryMappings();
			} catch (IOException e) {
				RegistriesImpl.LOGGER.error("Exception saving level", e);
			}
		}
	}
}
