package net.ornithemc.osl.biomes.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.gui.screen.world.CustomizeWorldScreen;
import net.minecraft.world.biome.Biome;

import net.ornithemc.osl.localization.api.L10n;

@Mixin(CustomizeWorldScreen.class)
public class CustomizeWorldScreenMixin {

	@WrapOperation(
		method = "formatValue(IF)Ljava/lang/String;",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/biome/Biome;name:Ljava/lang/String;"
		)
	)
	private String osl$biomes$translateBiomeName(Biome biome, Operation<String> op) {
		return L10n.get(op.call(biome));
	}
}
