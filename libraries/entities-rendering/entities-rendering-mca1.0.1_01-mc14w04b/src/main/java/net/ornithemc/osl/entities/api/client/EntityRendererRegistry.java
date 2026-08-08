package net.ornithemc.osl.entities.api.client;

import java.util.function.Supplier;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;

/**
 * A wrapper around the {@linkplain EntityRenderDispatcher} renderers map.
 */
@FunctionalInterface
public interface EntityRendererRegistry {

	/**
	 * Registers the given entity renderer for the given entity type.
	 * 
	 * @param <T>     the entity type for which to register the renderer.
	 * @param type    the entity type class.
	 * @param factory the factory for the entity renderer to register.
	 */
	<T extends Entity> void register(Class<T> type, Supplier<EntityRenderer<? super T>> factory);

	/**
	 * Registers the given entity renderer for the given entity type.
	 * <p>
	 * NOTE: this method only exists because generic type signatures are stripped
	 * from all Minecraft jars from 1.8.2-pre4 and below, which makes using
	 * {@linkplain #register(Class, Supplier)} impossible without special patches to
	 * bring these generic type signatures back. Progress on this work is being
	 * made, but has not yet been completed. In the meantime, this method exists to
	 * allow entity renderers to be registered without compile time type safety
	 * checks.
	 * 
	 * @param <T>     the entity type for which to register the renderer.
	 * @param type    the entity type class.
	 * @param factory the factory for the entity renderer to register.
	 * 
	 * @deprecated use {@linkplain #register(Class, Supplier)} if possible
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Deprecated
	default <T extends Entity> void registerUnsafe(Class<T> type, Supplier<EntityRenderer> factory) {
		this.register(type, (Supplier) factory);
	}
}
