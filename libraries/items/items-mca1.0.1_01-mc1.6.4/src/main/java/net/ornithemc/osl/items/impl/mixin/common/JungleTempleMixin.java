package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.gen.structure.LootEntry;
import net.minecraft.world.gen.structure.TemplePieces.JungleTemple;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.items.impl.item.LootTableMapper;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;

@Mixin(JungleTemple.class)
public class JungleTempleMixin {

	@Shadow
	private static LootEntry[] TREASURE_LOOT_ENTRIES;
	@Shadow
	private static LootEntry[] TRAP_LOOT_ENTRIES;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$items$registerLootTableMapper(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("loot/jungle_temple_treasure"), LootTableMapper.of(TREASURE_LOOT_ENTRIES));
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("loot/jungle_temple_trap"), LootTableMapper.of(TRAP_LOOT_ENTRIES));
	}
}
