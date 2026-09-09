package net.ornithemc.osl.blockentities.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.IdRegistry;

import net.ornithemc.osl.blockentities.impl.BlockEntityTypeIdRegistry;
import net.ornithemc.osl.blockentities.impl.BlockEntityTypeRegistryImpl;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {

	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "NEW",
			target = "net/minecraft/util/registry/IdRegistry"
		)
	)
	private static IdRegistry<Identifier, Class<? extends BlockEntity>> osl$blockentities$replaceIdRegistry() {
		// this allows us to register the block entity type registry in
		// the API entrypoint without triggering a BlockEntity class load
		return BlockEntityTypeIdRegistry.REGISTRY;
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
