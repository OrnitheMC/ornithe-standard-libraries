package net.ornithemc.osl.registries.impl.mixin.common;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.AlphaWorldStorage;
import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.registries.impl.access.ServerWorldAccess;
import net.ornithemc.osl.registries.impl.access.WorldStorageAccess;
import net.ornithemc.osl.registries.impl.registry.RegistriesImpl;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Conditional(minecraftVersion = @Version("<19w04a"))
	@Inject(
		method = "loadWorld",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/storage/WorldStorage;loadData()Lnet/minecraft/world/WorldData;"
		)
	)
	private void osl$registries$loadRegistryMappings(CallbackInfo ci, @Local WorldStorage storage) {
		if (storage instanceof WorldStorageAccess) {
			try {
				((WorldStorageAccess) storage).osl$registries$loadRegistryMappings();
			} catch (IOException e) {
				throw new CrashException(CrashReport.of(e, "Exception initializing level"));
			}
		}
	}

	@Conditional(minecraftVersion = @Version(">=19w04a"))
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

	@Conditional(minecraftVersion = @Version("<19w04a"))
	@Inject(
		method = "saveWorlds",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$registries$saveRegistryMappings(CallbackInfo ci) {
		this.saveRegistryMappings();
	}

	@Conditional(minecraftVersion = @Version(">=19w04a"))
	@Inject(
		method = "saveWorlds",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$registries$saveRegistryMappings(CallbackInfoReturnable<Boolean> cir) {
		this.saveRegistryMappings();
	}

	@Unique
	private void saveRegistryMappings() {
		@SuppressWarnings("resource")
		MinecraftServer server = (MinecraftServer) (Object) this;

		ServerWorld overworld = server.getWorld(DimensionType.OVERWORLD);
		WorldStorageAccess storage = ((ServerWorldAccess) overworld).accessStorage();

		if (storage != null) {
			try {
				((WorldStorageAccess) storage).osl$registries$saveRegistryMappings();
			} catch (IOException e) {
				RegistriesImpl.LOGGER.error("Exception saving level", e);
			}
		}
	}
}
