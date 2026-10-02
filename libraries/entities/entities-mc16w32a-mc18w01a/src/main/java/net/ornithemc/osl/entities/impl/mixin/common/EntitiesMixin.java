package net.ornithemc.osl.entities.impl.mixin.common;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ListMapper;

@Mixin(Entities.class)
public class EntitiesMixin {

	@Shadow
	private static List<String> NAMES;

	@Inject(
		method = "register",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$entities$register(int id, String key, Class<? extends Entity> type, String name, CallbackInfo ci) {
		EntityTypeRegistryImpl.REGISTRY.register(id, name, new Identifier(key), type);
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
