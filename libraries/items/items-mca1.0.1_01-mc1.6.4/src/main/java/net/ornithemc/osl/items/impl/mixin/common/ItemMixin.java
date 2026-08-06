package net.ornithemc.osl.items.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.items.api.item.ItemExtension;
import net.ornithemc.osl.items.impl.VanillaBlockItems;
import net.ornithemc.osl.items.impl.VanillaItems;
import net.ornithemc.osl.registries.api.registry.RegistryKeys;
import net.ornithemc.osl.registries.api.registry.SyncedRegistries;
import net.ornithemc.osl.registries.api.registry.sync.DynamicArrays;
import net.ornithemc.osl.registries.api.registry.sync.ObjectArrayMapper;

@Mixin(Item.class)
public class ItemMixin implements ItemExtension {

	@Shadow @Final @Mutable
	private static Item[] BY_ID;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$items$registerArrayMappers(CallbackInfo ci) {
		SyncedRegistries.registerMapper(RegistryKeys.ITEM, NamespacedIdentifiers.from("item/by_id"), ObjectArrayMapper.of(BY_ID));
	}

	@ModifyVariable(
		method = "<init>",
		argsOnly = true,
		ordinal = 0,
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Object;<init>()V",
			shift = Shift.AFTER
		)
	)
	private int osl$items$handleAutoAssignId(int id) {
		if (id == AUTO_ASSIGN_ID) {
			// the Item[] array must contain all items so this should
			// give us a valid ID for the Item registry to use.
			id = DynamicArrays.length(BY_ID);

			// keep 0-255 free for all Vanilla block items
			if (id <= VanillaBlockItems.MAX_ID) {
				id = VanillaBlockItems.MAX_ID + 1;
			}

			// we offset by -256 because the constructor logic adds 256
			id -= VanillaItems.ITEM_ID_OFFSET;
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
		// the constructor logic adds 256
		int capacity = (id + VanillaItems.ITEM_ID_OFFSET) + 1;

		BY_ID = DynamicArrays.grow(BY_ID, capacity);
	}
}
