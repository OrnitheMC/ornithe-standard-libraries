package net.ornithemc.osl.entities.impl.mixin.common;

import java.util.HashMap;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entities.SpawnEggData;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;
import net.ornithemc.osl.entities.impl.entity.SpawnEggDataMapper;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;

@Mixin(Entities.class)
public class EntitiesMixin_1_7_10 {

	@Shadow
	public static HashMap<Integer, SpawnEggData> SPAWN_EGG_DATA;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/entity/Entities;SPAWN_EGG_DATA:Ljava/util/HashMap;",
			opcode = Opcodes.PUTSTATIC,
			shift = Shift.AFTER
		)
	)
	private static void osl$entities$registerSpawnEggDataRegistry(CallbackInfo ci) {
		EntityTypeRegistryImpl.SPAWN_EGG_DATA = SPAWN_EGG_DATA;
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$entities$registerSpawnEggDataMapper(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.ENTITY_TYPE, NamespacedIdentifiers.from("entity_type/spawn_egg_data"), SpawnEggDataMapper.of(SPAWN_EGG_DATA));
	}
}
