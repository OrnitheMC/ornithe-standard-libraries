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

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.registries.impl.Bootstrap;
import net.ornithemc.osl.registries.impl.RegistriesEntrypoint;
import net.ornithemc.osl.registries.impl.access.WorldStorageAccess;
import net.ornithemc.osl.registries.impl.mixin.common.WorldAccess;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingStorage;

@Mixin(
	value = MinecraftServer.class,
	priority = 1001 // make sure entrypoints mixin is applied first
)
public class MinecraftServerMixin {

	@Conditional(minecraftVersion = @Version("<=b1.5_02"))
	@Shadow
	private ServerWorld f_58488472;
	@Conditional(minecraftVersion = @Version(">b1.5_02"))
	@Shadow
	private ServerWorld[] worlds;

	@Inject(
		method = "main",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$registries$bootstrap(CallbackInfo ci) {
		Bootstrap.init();
	}

	@Conditional(minecraftVersion = @Version(">=b1.3"))
	@WrapOperation(
		method = "loadWorld",
		at = @At(
			value = "NEW",
			target = "net/minecraft/world/storage/AnvilWorldStorage"
		)
	)
	private AnvilWorldStorage osl$registries$loadRegistryMappings1(File file, String saveName, boolean createPlayersDir, Operation<AnvilWorldStorage> op) {
		AnvilWorldStorage storage = op.call(file, saveName, createPlayersDir);

		if (storage instanceof WorldStorageAccess) {
			File dir = ((WorldStorageAccess) storage).osl$registries$getDirectory();

			try {
				RegistryMappingStorage.loadRegistryMappings(dir);
			} catch (IOException e) {
				throw new RuntimeException("Exception initializing level", e);
			}
		}

		return storage;
	}

	@Conditional(minecraftVersion = @Version("<b1.3"))
	@Inject(
		method = "saveWorlds",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$registries$saveRegistryMappings1(CallbackInfo ci) {
		File dir = ((WorldAccess) this.f_58488472).accessSaveDirectory();

		try {
			RegistryMappingStorage.saveRegistryMappings(dir);
		} catch (IOException e) {
			RegistriesImpl.LOGGER.error("Exception saving level", e);
		}
	}

	@Conditional(minecraftVersion = @Version(">=b1.3"))
	@Inject(
		method = "saveWorlds",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$registries$saveRegistryMappings2(CallbackInfo ci) {
		WorldStorage storage = RegistriesEntrypoint.SERVER_SUPPORTS_NETHER
			? this.worlds[0].getStorage()
			: this.f_58488472.getStorage();

		if (storage instanceof WorldStorageAccess) {
			File dir = ((WorldStorageAccess) storage).osl$registries$getDirectory();

			try {
				RegistryMappingStorage.saveRegistryMappings(dir);
			} catch (IOException e) {
				RegistriesImpl.LOGGER.error("Exception saving level", e);
			}
		}
	}
}
