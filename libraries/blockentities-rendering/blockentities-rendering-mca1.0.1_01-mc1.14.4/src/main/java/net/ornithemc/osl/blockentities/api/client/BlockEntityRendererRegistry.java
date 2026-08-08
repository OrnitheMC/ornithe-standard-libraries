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

	/**
	 * Registers the given block entity renderer for the given block entity type.
	 * <p>
	 * NOTE: this method only exists because generic type signatures are stripped
	 * from all Minecraft jars from 1.8.2-pre4 and below, which makes using
	 * {@linkplain #register(Class, Supplier)} impossible without special patches to
	 * bring these generic type signatures back. Progress on this work is being
	 * made, but has not yet been completed. In the meantime, this method exists to
	 * allow block entity renderers to be registered without compile time type
	 * safety checks.
	 * 
	 * @param <T>     the block entity type for which to register the renderer.
	 * @param type    the block entity type class.
	 * @param factory the factory for the block entity renderer to register.
	 * 
	 * @deprecated use {@linkplain #register(Class, Supplier)} if possible
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Deprecated
	default <T extends BlockEntity> void registerUnsafe(Class<T> type, Supplier<BlockEntityRenderer> factory) {
		this.register(type, (Supplier) factory);
	}
}
