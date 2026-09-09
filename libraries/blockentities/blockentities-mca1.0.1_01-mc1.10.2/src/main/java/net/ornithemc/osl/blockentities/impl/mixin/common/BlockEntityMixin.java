package net.ornithemc.osl.blockentities.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;

import net.ornithemc.osl.blockentities.impl.BlockEntityTypeRegistryImpl;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {

	@Inject(
		method = "register",
		at = @At(
			value = "TAIL"
		)
	)
	private static void osl$blockentities$register(Class<? extends BlockEntity> type, String legacyId, CallbackInfo ci) {
		BlockEntityTypeRegistryImpl.REGISTRY.register(legacyId, type);
	}

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
