package net.ornithemc.osl.entities.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.animal.ChickenEntity;
import net.minecraft.entity.mob.animal.CowEntity;

import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;

@Mixin(Entities.class)
public class EntitiesMixinNew {

	@Inject(
		method = "register",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$entities$register(Class<? extends Entity> type, String legacyId, int id, CallbackInfo ci) {
		// there is a bug in a1.1.x where sheep, cows, and chickens
		// all have ID 91, which causes all sheep and cows to appear
		// as chickens client-side when connected to a remote server
		// it also breaks the OSL registry so we fix that bug here
		if (id == 91) {
			if (type == CowEntity.class) {
				id = 92;
			}
			if (type == ChickenEntity.class) {
				id = 93;
			}
		}

		EntityTypeRegistryImpl.REGISTRY.register(id, legacyId, type);
	}
}
