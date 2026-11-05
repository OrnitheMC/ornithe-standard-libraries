package net.ornithemc.osl.registries.impl.mixin.client;

import java.io.File;
import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.registries.impl.Bootstrap;
import net.ornithemc.osl.registries.impl.access.WorldStorageAccess;
import net.ornithemc.osl.registries.impl.mixin.common.WorldAccess;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;
import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingStorage;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	private World world;

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Object;<init>()V",
			shift = Shift.AFTER
		)
	)
	private void osl$registries$bootstrap(CallbackInfo ci) {
		Bootstrap.init();
	}

	@Conditional(minecraftVersion = @Version(">=b1.3"))
	@Inject(
		method = "startGame",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/storage/WorldStorageSource;get(Ljava/lang/String;Z)Lnet/minecraft/world/storage/WorldStorage;",
			shift = Shift.BY,
			by = 2
		)
	)
	private void osl$registries$loadRegistryMappings(CallbackInfo ci, @Local WorldStorage storage) {
		if (storage instanceof WorldStorageAccess) {
			File dir = ((WorldStorageAccess) storage).osl$registries$getDirectory();

			try {
				RegistryMappingStorage.loadRegistryMappings(dir);
			} catch (IOException e) {
				throw new RuntimeException("Exception initializing level", e);
			}
		}
	}

	@Conditional(minecraftVersion = @Version("<b1.3"))
	@Inject(
		method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;)V",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$registries$saveRegistryMappings1(World world, String message, CallbackInfo ci) {
		if (this.world != null && !this.world.isMultiplayer && world == null) {
			File dir = ((WorldAccess) this.world).accessSaveDirectory();

			try {
				RegistryMappingStorage.saveRegistryMappings(dir);
			} catch (IOException e) {
				RegistriesImpl.LOGGER.error("Exception saving level", e);
			}
		}
	}

	@Conditional(minecraftVersion = @Version(">=b1.3"))
	@Inject(
		method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;)V",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$registries$saveRegistryMappings2(World world, String message, CallbackInfo ci) {
		if (this.world != null && !this.world.isMultiplayer && world == null) {
			WorldStorage storage = ((WorldAccess) this.world).accessStorage();

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
}
