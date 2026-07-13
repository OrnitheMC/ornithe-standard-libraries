package net.ornithemc.osl.blocks.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;

@Mixin(Block.class)
public class BlockMixin {

	@Inject(
		method = "init",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$blocks$unlockBlockRegistry(CallbackInfo ci) {
		BlockRegistryImpl.unlock();
	}

	@Inject(
		method = "init",
		slice = @Slice(
			from = @At(
				value = "CONSTANT",
				args = "stringValue=structure_block" // last block to be registered
			)
		),
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/Block;register(Ljava/lang/String;Lnet/minecraft/block/Block;)V",
			shift = Shift.AFTER
		)
	)
	private static void osl$blocks$registerBlocks(CallbackInfo ci) {
		BlockRegistryImpl.registerBlocks();
	}
}
