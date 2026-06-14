package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;

import net.ornithemc.osl.items.api.item.ItemExtension;
import net.ornithemc.osl.items.impl.ItemRegistryImpl;

@Mixin(Item.class)
public class ItemMixinNew implements ItemExtension {

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$items$unlockItemRegistry(CallbackInfo ci) {
		ItemRegistryImpl.unlock();
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/stat/Stats;initItemStats()V"
		)
	)
	private static void osl$items$initAndLockItemRegistry(CallbackInfo ci) {
		ItemRegistryImpl.initItems();

		if (ItemRegistryImpl.shouldInitialize()) {
			ItemRegistryImpl.init();
			ItemRegistryImpl.lock();
		}
	}
}
