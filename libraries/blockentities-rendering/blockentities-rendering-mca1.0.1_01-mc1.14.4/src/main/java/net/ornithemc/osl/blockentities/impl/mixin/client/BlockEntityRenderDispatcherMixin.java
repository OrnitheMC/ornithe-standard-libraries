package net.ornithemc.osl.blockentities.impl.mixin.client;

import java.util.Map;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

import net.ornithemc.osl.blockentities.api.client.BlockEntityRenderingEvents;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {

	@Shadow @Final
	private Map<Class<? extends BlockEntity>, BlockEntityRenderer<? extends BlockEntity>> renderers;

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Map;values()Ljava/util/Collection;"
		)
	)
	private void osl$blockentities$registerBlockEntityRenderers(CallbackInfo ci) {
		BlockEntityRenderingEvents.REGISTER_BLOCK_ENTITY_RENDERERS.invoker().accept(this::register);
	}

	@Unique
	private <T extends BlockEntity> void register(Class<T> type, Supplier<BlockEntityRenderer<? super T>> factory) {
		this.renderers.put(type, factory.get());
	}
}
