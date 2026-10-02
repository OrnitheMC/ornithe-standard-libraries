package net.ornithemc.osl.biomes.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.gui.overlay.DebugOverlay;
import net.minecraft.world.biome.Biome;

@Mixin(DebugOverlay.class)
public class DebugOverlayMixin {

	@WrapOperation(
		method = "getGameInfo",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/biome/Biome;name:Ljava/lang/String;"
		)
	)
	private String osl$biomes$translateBiomeName(Biome biome, Operation<String> op) {
		return biome.getName();
	}
}
