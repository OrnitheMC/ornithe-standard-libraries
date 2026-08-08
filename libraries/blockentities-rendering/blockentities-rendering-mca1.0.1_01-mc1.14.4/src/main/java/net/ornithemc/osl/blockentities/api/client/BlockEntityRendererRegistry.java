package net.ornithemc.osl.blockentities.api.client;

import java.util.function.Supplier;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

/**
 * A wrapper around the {@linkplain BlockEntityRenderDispatcher} renderers map.
 */
@FunctionalInterface
public interface BlockEntityRendererRegistry {

	/**
	 * Registers the given block entity renderer for the given block entity type.
	 * 
	 * @param <T>     the block entity type for which to register the renderer.
	 * @param type    the block entity type class.
	 * @param factory the factory for the block entity renderer to register.
	 */
	<T extends BlockEntity> void register(Class<T> type, Supplier<BlockEntityRenderer<? super T>> factory);

}
