package net.ornithemc.osl.entities.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;

import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;

@Mixin(Entities.class)
public class EntitiesMixin {

	@Inject(
		method = "register",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$entities$register(Class<? extends Entity> type, String legacyKey, int id, CallbackInfo ci) {
		EntityTypeRegistryImpl.REGISTRY.register(id, legacyKey, type);
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$entities$unlockEntityTypeRegistry(CallbackInfo ci) {
		EntityTypeRegistryImpl.unlock();
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$entities$registerEntityTypes(CallbackInfo ci) {
		EntityTypeRegistryImpl.registerEntityTypes();
	}
}
