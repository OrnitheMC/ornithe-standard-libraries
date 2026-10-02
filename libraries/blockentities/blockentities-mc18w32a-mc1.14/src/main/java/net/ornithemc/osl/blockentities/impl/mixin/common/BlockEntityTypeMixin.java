package net.ornithemc.osl.blockentities.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntityType;

import net.ornithemc.osl.blockentities.impl.BlockEntityTypeRegistryImpl;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "HEAD"
		)
	)
	private static void osl$blockentities$unlockBlockEntityTypeRegistry(CallbackInfo ci) {
		BlockEntityTypeRegistryImpl.unlock();
	}

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$blockentities$registerBlockEntityTypes(CallbackInfo ci) {
		BlockEntityTypeRegistryImpl.registerBlockEntityTypes();
	}
}
