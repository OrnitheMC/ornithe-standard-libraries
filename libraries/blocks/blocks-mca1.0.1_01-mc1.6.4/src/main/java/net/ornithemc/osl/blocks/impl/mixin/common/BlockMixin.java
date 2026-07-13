package net.ornithemc.osl.blocks.impl.mixin.common;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;

@Mixin(Block.class)
public class BlockMixin {

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
			// inject before the for-loop for checking USES_NEIGHBOR_LIGHT
			value = "JUMP",
			opcode = Opcodes.IF_ICMPGE,
			shift = Shift.BY,
			// it's extreme but this ensures that the injector is only invoked once
			by = -5
		)
	)
	private static void osl$blocks$registerBlocks(CallbackInfo ci) {
		// in Beta 1.2 and above Item class init will happen before this point
		// but that should not matter as there is a separate event for block
		// item registration
		BlockRegistryImpl.registerBlocks();
	}

	@Override
	public String toString() {
		return "Block{" + BlockRegistryImpl.getIdentifier((Block) (Object) this) + "}";
	}
}
