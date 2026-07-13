package net.ornithemc.osl.blocks.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Blocks;

import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;

@Mixin(Blocks.class)
public class BlocksMixin {

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$blocks$unlockBlockRegistry(CallbackInfo ci) {
		BlockRegistryImpl.unlock();
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/util/registry/Registry;BLOCK:Lnet/minecraft/util/registry/DefaultedIdRegistry;"
		)
	)
	private static void osl$blocks$registerBlocks(CallbackInfo ci) {
		BlockRegistryImpl.registerBlocks();
	}
}
