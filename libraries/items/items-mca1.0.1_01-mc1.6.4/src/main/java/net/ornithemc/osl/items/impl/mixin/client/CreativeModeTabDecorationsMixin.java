package net.ornithemc.osl.items.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.PlantBlock;

import net.ornithemc.osl.items.impl.item.ItemUtil;

@Mixin(targets = {
	"net/minecraft/item/CreativeModeTab$35315259" // decorations
})
public class CreativeModeTabDecorationsMixin {

	@WrapOperation(
		method = "getIconItem",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/PlantBlock;id:I"
		)
	)
	private int osl$items$fixIconItem(PlantBlock block, Operation<Integer> op) {
		return ItemUtil.itemId(block);
	}
}
