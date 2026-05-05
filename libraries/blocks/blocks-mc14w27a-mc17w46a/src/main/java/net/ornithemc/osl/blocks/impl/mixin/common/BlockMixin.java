package net.ornithemc.osl.blocks.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.api.block.BlockExtension;
import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;

@Mixin(Block.class)
public class BlockMixin implements BlockExtension {

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
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/registry/DefaultedIdRegistry;validate()V"
		)
	)
	private static void osl$blocks$initAndLockBlockRegistry(CallbackInfo ci) {
		BlockRegistryImpl.init();
		BlockRegistryImpl.lock();
	}

	@Override
	public boolean isAir() {
		return false;
	}
}
