package net.ornithemc.osl.entities.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;

import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;

@Mixin(Entities.class)
public class EntitiesMixinOld {

	@Inject(
		method = "register",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$entities$register(CallbackInfo ci, @Local Class<? extends Entity> type, @Local String legacyKey) {
		EntityTypeRegistryImpl.REGISTRY.register(legacyKey, type);
	}
}
