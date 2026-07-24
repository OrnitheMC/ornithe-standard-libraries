package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ObjectArrayMapper;

@Mixin(Stats.class)
public class StatsMixin {

	@Shadow @Final
	private static Stat[] ITEMS_CRAFTED;
	@Shadow @Final
	private static Stat[] ITEMS_USED;
	@Shadow @Final
	private static Stat[] ITEMS_BROKEN;

	@Inject(
		// inject at init instead of <clinit> in case the arrays are resized...
		method = "init",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$items$registerStatsMapper(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/crafted"), ObjectArrayMapper.of(ITEMS_CRAFTED));
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/used"), ObjectArrayMapper.of(ITEMS_USED));
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/broken"), ObjectArrayMapper.of(ITEMS_BROKEN));
	}
}
