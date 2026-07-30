package net.ornithemc.osl.blocks.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.blocks.api.block.BlockExtension;
import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;
import net.ornithemc.osl.blocks.impl.BlocksMixinPlugin;
import net.ornithemc.osl.blocks.impl.block.AirBlock;
import net.ornithemc.osl.blocks.impl.block.BlockPostInit;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.Util;

@Mixin(Block.class)
public class BlockMixin implements BlockExtension {

	@Shadow
	private String key;

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
	private static void osl$blocks$registerBlocks(CallbackInfo ci) {
		BlockRegistryImpl.registerBlocks();

		for (Block block : Block.REGISTRY) {
			if (block instanceof BlockPostInit) {
				((BlockPostInit) block).osl$blocks$postInit();
			}
		}
	}

	@Inject(
		method = "getTranslationKey",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$blocks$autoAssignTranslationKey(CallbackInfoReturnable<String> cir) {
		if (this.key == null) {
			NamespacedIdentifier identifier = BlockRegistry.getIdentifier((Block) (Object) this);

			if (identifier == null) {
				this.key = "unknown";
			} else {
				this.key = Util.makeTranslationKey(identifier);
			}
		}
	}

	@Override
	public String toString() {
		return "Block{" + BlockRegistryImpl.getIdentifier((Block) (Object) this) + "}";
	}

	@Override
	public boolean isAir() {
		return false;
	}
}
