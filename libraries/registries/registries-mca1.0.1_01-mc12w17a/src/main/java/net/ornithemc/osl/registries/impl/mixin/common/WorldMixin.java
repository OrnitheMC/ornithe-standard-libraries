package net.ornithemc.osl.registries.impl.mixin.common;

import java.io.File;
import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.World;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.GameSide;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.registries.impl.registry.sync.RegistryMappingStorage;

@Conditional(minecraftVersion = @Version("<b1.3"))
@Mixin(World.class)
public class WorldMixin {

	@Shadow
	private File f_14667040; // saveDir

	@Conditional(minecraftVersion = {
		@Version(value = "<a1.2.0", gameSide = GameSide.CLIENT),
		@Version(value = "<a0.2.2", gameSide = GameSide.SERVER)
	})
	@Inject(
		method = "<init>(Ljava/io/File;Ljava/lang/String;J)V",
		at = @At(
			value = "CONSTANT",
			args = "stringValue=level.dat"
		)
	)
	private void osl$registries$loadRegistryMappings1(CallbackInfo ci) {
		try {
			RegistryMappingStorage.loadRegistryMappings(this.f_14667040);
		} catch (IOException e) {
			throw new RuntimeException("Exception initializing level", e);
		}
	}

	@Conditional(minecraftVersion = {
		@Version(value = ">=a1.2.0", gameSide = GameSide.CLIENT),
		@Version(value = ">=a0.2.2", gameSide = GameSide.SERVER)
	})
	@Inject(
		method = "<init>(Ljava/io/File;Ljava/lang/String;JLnet/minecraft/unmapped/C_90532916;)V",
		at = @At(
			value = "CONSTANT",
			args = "stringValue=level.dat"
		)
	)
	private void osl$registries$loadRegistryMappings2(CallbackInfo ci) {
		try {
			RegistryMappingStorage.loadRegistryMappings(this.f_14667040);
		} catch (IOException e) {
			throw new RuntimeException("Exception initializing level", e);
		}
	}
}
