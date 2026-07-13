package net.ornithemc.osl.registries.impl.mixin.common;

import java.io.File;
import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.World;

import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingStorage;

@Mixin(World.class)
public class WorldMixinOld {

	@Shadow
	private File f_14667040; // saveDir

	@Inject(
		method = "<init>(Ljava/io/File;Ljava/lang/String;J)V",
		at = @At(
			value = "CONSTANT",
			args = "stringValue=level.dat"
		)
	)
	private void osl$registries$loadRegistryMappings(CallbackInfo ci) {
		try {
			RegistryMappingStorage.loadRegistryMappings(this.f_14667040);
		} catch (IOException e) {
			throw new RuntimeException("Exception initializing level", e);
		}
	}
}
