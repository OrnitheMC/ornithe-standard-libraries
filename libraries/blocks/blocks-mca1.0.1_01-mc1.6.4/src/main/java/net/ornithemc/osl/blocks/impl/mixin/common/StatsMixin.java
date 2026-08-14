package net.ornithemc.osl.blocks.impl.mixin.common;

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
	private static Stat[] BLOCKS_MINED;

	@Inject(
		method = "initItemsCraftedStats",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/stat/Stats;mergeBlockStats([Lnet/minecraft/stat/Stat;)V"
		)
	)
	private static void osl$items$registerStatsMapper(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.BLOCK, NamespacedIdentifiers.from("stats/mined"), ObjectArrayMapper.of(BLOCKS_MINED));
	}
}
