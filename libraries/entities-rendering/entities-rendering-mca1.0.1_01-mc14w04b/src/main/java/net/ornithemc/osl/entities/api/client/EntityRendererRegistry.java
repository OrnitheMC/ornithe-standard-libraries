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

}
