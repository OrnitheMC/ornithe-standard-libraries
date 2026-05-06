package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Items;

import net.ornithemc.osl.items.impl.ItemRegistryImpl;

@Mixin(Items.class)
public class ItemsMixin {

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
			value = "TAIL"
		)
	)
	private static void osl$items$initAndLockItemRegistry(CallbackInfo ci) {
		ItemRegistryImpl.init();
		ItemRegistryImpl.lock();
	}
}
