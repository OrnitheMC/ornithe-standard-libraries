package net.ornithemc.osl.items.impl.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.living.mob.passive.VillagerEntity;
import net.minecraft.util.Pair;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.IntegerMapMapper;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {

	@Shadow
	private static Map<Integer, Pair<Integer, Integer>> BUY_OFFERS;
	@Shadow
	private static Map<Integer, Pair<Integer, Integer>> SELL_OFFERS;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$items$registerTradeOfferMappers(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("trade/buy_offer"), IntegerMapMapper.of(BUY_OFFERS));
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("trade/sell_offer"), IntegerMapMapper.of(SELL_OFFERS));
	}
}
