package net.ornithemc.osl.items.impl.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.crafting.SmeltingManager;
import net.minecraft.item.ItemStack;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.items.impl.item.SmeltingRecipesMapper;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;

@Mixin(SmeltingManager.class)
public class SmeltingManagerMixin {

	@Shadow
	private Map<Integer, ItemStack> recipes;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$items$registerRecipesMapper(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("smelting/recipe"), SmeltingRecipesMapper.of(this.recipes));
	}
}
