package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.IdRegistry;

import net.ornithemc.osl.items.impl.ItemRegistryImpl;

@Mixin(Item.class)
public class ItemMixinOld {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$items$unlockItemRegistry(CallbackInfo ci) {
		ItemRegistryImpl.unlock();
	}

	@Inject(
		method = "init",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/registry/IdRegistry;keySet()Ljava/util/Set;"
		)
	)
	private static void osl$items$registerItems(CallbackInfo ci) {
		ItemRegistryImpl.registerItems();
	}

	@WrapOperation(
		method = "init",
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/util/registry/IdRegistry;keySet()Ljava/util/Set;"
			)
		),
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/registry/IdRegistry;register(ILjava/lang/String;Ljava/lang/Object;)V"
		)
	)
	private static void osl$items$registerBlockItems(IdRegistry<Item> registry, int id, String key, Object value, Operation<Void> operation, @Local Block block, @Local BlockItem item) {
		if (item.getClass() != BlockItem.class || !ItemRegistryImpl.BLOCK_ITEMS.containsKey(block)) {
			operation.call(registry, id, key, value);
		}
	}
}
