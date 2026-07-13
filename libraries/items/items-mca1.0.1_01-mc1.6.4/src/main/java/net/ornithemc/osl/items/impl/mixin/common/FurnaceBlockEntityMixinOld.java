package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.Block;
import net.minecraft.block.entity.FurnaceBlockEntity;

import net.ornithemc.osl.items.impl.item.ItemUtil;

@Mixin(FurnaceBlockEntity.class)
public class FurnaceBlockEntityMixinOld {

	@WrapOperation(
		method = "m_37310100", // getResult
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/block/Block;id:I"
		)
	)
	private int osl$items$fixBlockItem(Block block, Operation<Integer> op) {
		return ItemUtil.itemId(block);
	}
}
