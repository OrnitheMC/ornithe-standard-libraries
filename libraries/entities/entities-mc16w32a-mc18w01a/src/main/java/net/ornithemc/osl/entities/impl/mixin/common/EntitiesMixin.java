package net.ornithemc.osl.entities.impl.mixin.common;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;
import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.IdRegistry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.entities.impl.EntityTypeIdRegistry;
import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ListMapper;

@Mixin(Entities.class)
public class EntitiesMixin {

	@Shadow
	private static List<String> NAMES;

	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "NEW",
			target = "net/minecraft/util/registry/IdRegistry"
		)
	)
	private static IdRegistry<Identifier, Class<? extends Entity>> osl$entities$replaceIdRegistry() {
		// this allows us to register the entity type registry in the
		// API entrypoint without triggering an Entities class load
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

		SyncedRegistries.registerMapper(RegistryKeys.ENTITY_TYPE, NamespacedIdentifiers.from("entity_type/name"), ListMapper.of(NAMES));
	}
}
