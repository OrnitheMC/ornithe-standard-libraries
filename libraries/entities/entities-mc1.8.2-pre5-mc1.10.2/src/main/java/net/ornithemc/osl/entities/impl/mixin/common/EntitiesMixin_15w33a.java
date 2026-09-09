package net.ornithemc.osl.entities.impl.mixin.common;

import java.util.Map;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entities;
import net.minecraft.entity.Entities.SpawnEggData;

import net.ornithemc.osl.entities.api.EntityTypeRegistry;
import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;

@Mixin(Entities.class)
public class EntitiesMixin_15w33a {

	@Shadow
	public static Map<String, SpawnEggData> SPAWN_EGG_DATA;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/entity/Entities;SPAWN_EGG_DATA:Ljava/util/Map;",
			opcode = Opcodes.PUTSTATIC,
			shift = Shift.AFTER
		)
	)
	private static void osl$entities$registerSpawnEggDataRegistry(CallbackInfo ci) {
		EntityTypeRegistryImpl.SPAWN_EGG_DATA_REGISTRY = (type, baseColor, spotsColor) -> {
			String legacyKey = EntityTypeRegistry.getLegacyKey(type);

			if (legacyKey == null) {
				throw new IllegalArgumentException("Entity type " + type.getSimpleName() + " is not registered!");
			}
			if (!SPAWN_EGG_DATA.containsKey(legacyKey)) {
				throw new IllegalArgumentException("Duplicate entity type legacy key " + legacyKey + " in spawn egg data registry!");
			}

			SPAWN_EGG_DATA.put(legacyKey, SpawnEggDataAccessNew.of(legacyKey, baseColor, spotsColor));
		};
	}
}
