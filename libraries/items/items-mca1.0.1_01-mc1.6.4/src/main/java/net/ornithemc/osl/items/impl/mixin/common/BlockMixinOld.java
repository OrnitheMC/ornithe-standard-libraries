package net.ornithemc.osl.items.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;

import net.ornithemc.osl.items.impl.ItemRegistryImpl;

@Mixin(Block.class)
public class BlockMixinOld {

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$items$initBlockItems(CallbackInfo ci) {
		ItemRegistryImpl.initBlocks();
	}
}
