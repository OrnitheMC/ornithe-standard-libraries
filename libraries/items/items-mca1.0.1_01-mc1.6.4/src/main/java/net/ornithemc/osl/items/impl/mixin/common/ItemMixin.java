package net.ornithemc.osl.items.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;

import net.ornithemc.osl.items.api.item.ItemExtension;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArrays;

@Mixin(Item.class)
public class ItemMixin implements ItemExtension {

	@Shadow @Final @Mutable
	private static Item[] BY_ID;

	@ModifyVariable(
		method = "<init>",
		argsOnly = true,
		ordinal = 0,
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/item/Item;BY_ID:[Lnet/minecraft/item/Item;",
			ordinal = 0
		)
	)
	private int osl$items$handleAutoAssignId(int id) {
		if (id == AUTO_ASSIGN_ID) {
			// the Block[] array must contain all blocks so this should
			// give us a valid ID for the Block registry to use.
			id = DynamicArrays.length(BY_ID);
		}

		return id;
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/item/Item;BY_ID:[Lnet/minecraft/item/Item;",
			opcode = Opcodes.GETSTATIC,
			args = "array=set"
		)
	)
	private void osl$items$growArrays(int id, CallbackInfo ci) {
		int capacity = id + 1;

		BY_ID = DynamicArrays.grow(BY_ID, capacity);
	}
}
