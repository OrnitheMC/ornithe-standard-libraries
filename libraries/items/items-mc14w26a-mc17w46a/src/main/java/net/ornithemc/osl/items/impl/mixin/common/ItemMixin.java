package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.Item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.items.api.ItemRegistry;
import net.ornithemc.osl.items.impl.ItemRegistryImpl;

@Mixin(Item.class)
public class ItemMixin {

	@Shadow
	private String key;

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
			value = "TAIL"
		)
	)
	private static void osl$items$registerItems(CallbackInfo ci) {
		ItemRegistryImpl.registerItems();
	}

	@Inject(
		method = "getTranslationKey()Ljava/lang/String;",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$items$autoAssignTranslationKey1(CallbackInfoReturnable<String> cir) {
		this.ensureTranslationKeyExists();
	}

	@Inject(
		method = "getTranslationKey(Lnet/minecraft/item/ItemStack;)Ljava/lang/String;",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$items$autoAssignTranslationKey2(CallbackInfoReturnable<String> cir) {
		this.ensureTranslationKeyExists();
	}

	@Unique
	private void ensureTranslationKeyExists() {
		if (this.key == null) {
			NamespacedIdentifier identifier = ItemRegistry.getIdentifier((Item) (Object) this);

			if (identifier == null) {
				this.key = "unknown";
			} else {
				this.key = Util.makeTranslationKey(identifier);
			}
		}
	}
}
