package net.ornithemc.osl.entities.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.IdRegistry;

import net.ornithemc.osl.entities.impl.EntityTypeIdRegistry;
import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;

@Mixin(EntityType.class)
public class EntityTypeMixin {

	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "NEW",
			target = "net/minecraft/util/registry/IdRegistry"
		)
	)
	private static IdRegistry<Identifier, EntityType<?>> osl$entities$replaceIdRegistry() {
		// this allows us to register the entity type registry in the
		// API entrypoint without triggering an EntityType class load
		return EntityTypeIdRegistry.REGISTRY;
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
