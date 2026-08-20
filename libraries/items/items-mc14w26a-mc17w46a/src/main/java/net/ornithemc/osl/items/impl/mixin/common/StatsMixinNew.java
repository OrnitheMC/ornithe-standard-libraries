package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.ArrayMapper;

@Mixin(Stats.class)
public class StatsMixinNew {

	@Shadow @Final @Mutable
	private static Stat[] ITEMS_PICKED_UP;
	@Shadow @Final @Mutable
	private static Stat[] ITEMS_DROPPED;

	@Inject(
		// inject at init instead of <clinit> in case the arrays are resized...
		method = "init",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$items$registerStatsMapper(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/picked_up"), ArrayMapper.of(() -> ITEMS_PICKED_UP, a -> ITEMS_PICKED_UP = a));
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("stats/dropped"), ArrayMapper.of(() -> ITEMS_DROPPED, a -> ITEMS_DROPPED = a));
	}
}
