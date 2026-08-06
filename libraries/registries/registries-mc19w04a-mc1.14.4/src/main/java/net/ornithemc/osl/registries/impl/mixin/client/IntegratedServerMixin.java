package net.ornithemc.osl.registries.impl.mixin.client;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.world.storage.AlphaWorldStorage;

import net.ornithemc.osl.registries.impl.access.WorldStorageAccess;
import net.ornithemc.osl.registries.impl.registry.SyncedRegistriesImpl;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

	@Inject(
		method = "loadWorld",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/storage/AlphaWorldStorage;m_16306786()Lnet/minecraft/world/WorldData;"
		)
	)
	private void osl$registries$loadRegistryMappings(CallbackInfo ci, @Local AlphaWorldStorage storage) {
		if (storage instanceof WorldStorageAccess) {
			try {
				((WorldStorageAccess) storage).osl$registries$loadRegistryMappings();
			} catch (IOException e) {
				throw new CrashException(CrashReport.of(e, "Exception initializing level"));
			}
		}
	}

	@Inject(
		method = "shutdown",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$registries$unmapRegistries(CallbackInfo ci) {
		SyncedRegistriesImpl.undoMappings();
	}
}
