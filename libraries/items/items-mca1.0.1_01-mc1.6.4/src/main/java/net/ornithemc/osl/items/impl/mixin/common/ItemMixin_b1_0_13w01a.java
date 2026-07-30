package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.Item;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.Util;
import net.ornithemc.osl.items.api.ItemRegistry;

@Mixin(Item.class)
public class ItemMixin_b1_0_13w01a {

	@Shadow
	private String key;

	@Inject(
		method = "getTranslationKey()Ljava/lang/String;",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$items$autoAssignTranslationKey(CallbackInfoReturnable<String> cir) {
		if (this.key == null) {
			NamespacedIdentifier identifier = ItemRegistry.getIdentifier((Item) (Object) this);

			if (identifier == null) {
				this.key = "item.unknown";
			} else {
				this.key = Util.makeTranslationKey("item", identifier);
			}
		}
	}
}
