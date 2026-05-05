package net.ornithemc.osl.blocks.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.api.block.BlockExtension;
import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;
import net.ornithemc.osl.blocks.impl.BlocksMixinPlugin;
import net.ornithemc.osl.blocks.impl.block.AirBlock;

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

		if (!BlocksMixinPlugin.AIR_BLOCK_EXISTS) {
			Block.REGISTRY.register(0, "air", new AirBlock());
		}
	}

	@Inject(
		method = "init",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/registry/IdRegistry;iterator()Ljava/util/Iterator;"
		)
	)
	private static void osl$blocks$initAndLockBlockRegistry(CallbackInfo ci) {
		BlockRegistryImpl.init();
		BlockRegistryImpl.lock();
	}

	@Override
	public String toString() {
		return "Block{" + BlockRegistryImpl.getKey((Block) (Object) this) + "}";
	}

	@Override
	public boolean isAir() {
		return false;
	}
}
