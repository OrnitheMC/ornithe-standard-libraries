package net.ornithemc.osl.items.impl.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ArrayMapper;
import net.ornithemc.osl.registries.impl.registry.sync.StatsMapper;

@Mixin(Stats.class)
public class StatsMixin {

	@Shadow @Final
	private static Map<String, Stat> BY_KEY;
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
		if (MinecraftVersion.resolve().compareTo("14w06a") < 0) {
			SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/crafted"), StatsMapper.of(BY_KEY, ITEMS_CRAFTED));
			SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/used"), StatsMapper.of(BY_KEY, ITEMS_USED));
			SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/broken"), StatsMapper.of(BY_KEY, ITEMS_BROKEN));
		} else {
			SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/crafted"), ArrayMapper.of(ITEMS_CRAFTED));
			SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/used"), ArrayMapper.of(ITEMS_USED));
			SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/broken"), ArrayMapper.of(ITEMS_BROKEN));
		}
	}
}
