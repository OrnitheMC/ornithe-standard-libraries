package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.registry.IdRegistry;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.items.api.ItemRegistry;
import net.ornithemc.osl.items.impl.ItemRegistryImpl;

@Mixin(Item.class)
public class ItemMixin_14w21b {

	@Overwrite
	public static Item byBlock(Block block) {
		return ItemRegistryImpl.BLOCK_ITEMS.get(block);
	}

	@WrapOperation(
		method = "init",
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/block/Block;getId(Lnet/minecraft/block/Block;)I"
			)
		),
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/registry/IdRegistry;register(ILjava/lang/String;Ljava/lang/Object;)V"
		)
	)
	private static void osl$items$registerVanillaBlockItem(IdRegistry<Item> registry, int id, String key, Object value, Operation<Void> op, @Local Block block) {
		op.call(registry, id, key, value);

		Item item = (Item) value;
		NamespacedIdentifier identifier = ItemRegistry.getIdentifier(item);

		if (identifier != null) {
			ItemRegistryImpl.BLOCK_ITEMS.put(block, item);
		}
	}
}
