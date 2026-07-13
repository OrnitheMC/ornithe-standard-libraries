package net.ornithemc.osl.items.impl.mixin.common;

import java.util.Comparator;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.crafting.CraftingManager;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.items.impl.item.CraftingRecipesMapper;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;

@Mixin(CraftingManager.class)
public class CraftingManagerMixin {

	@Shadow
	private List<?> recipes;

	@Unique
	private Comparator<?> sorter;

	@WrapOperation(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V"
		)
	)
	private void osl$items$captureSorter(List<?> recipes, Comparator<?> sorter, Operation<Void> op) {
		op.call(recipes, this.sorter = sorter);
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$items$registerRecipesMapper(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("crafting/recipe"), CraftingRecipesMapper.of(this.recipes, this.sorter));
	}
}
