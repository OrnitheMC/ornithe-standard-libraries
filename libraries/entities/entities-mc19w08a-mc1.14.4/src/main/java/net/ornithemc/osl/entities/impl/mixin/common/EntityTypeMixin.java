package net.ornithemc.osl.entities.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;

import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;

@Mixin(EntityType.class)
public class EntityTypeMixin {

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
