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

import net.ornithemc.osl.entities.impl.EntityTypeRegistryImpl;
import net.ornithemc.osl.entities.impl.SpawnEggDataRegistry;

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
		EntityTypeRegistryImpl.SPAWN_EGG_DATA = new SpawnEggDataRegistry() {

			@Override
			public void put(int id, String legacyId, int baseColor, int spotsColor) {
				SPAWN_EGG_DATA.put(legacyId, new SpawnEggData(id, baseColor, spotsColor));
			}

			@Override
			public boolean contains(int id, String legacyId) {
				return SPAWN_EGG_DATA.containsKey(legacyId);
			}

			@Override
			public SpawnEggData get(int id, String legacyId) {
				return SPAWN_EGG_DATA.get(legacyId);
			}
		};
	}
}
