package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.item.Item;
import net.minecraft.world.gen.chunk.FlatWorldLayer;

import net.ornithemc.osl.items.impl.item.ItemUtil;

@Mixin(targets = "net/minecraft/client/gui/screen/CustomizeFlatWorldScreen$LayerListWidget")
public class CustomizeFlatWorldScreen_LayerListWidgetMixin {

	@WrapOperation(
		method = "renderEntry",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/chunk/FlatWorldLayer;getBlock()I"
		)
	)
	private int osl$items$fixBlockItem(FlatWorldLayer layer, Operation<Item> op) {
		return ItemUtil.itemId(layer.getBlock());
	}
}
