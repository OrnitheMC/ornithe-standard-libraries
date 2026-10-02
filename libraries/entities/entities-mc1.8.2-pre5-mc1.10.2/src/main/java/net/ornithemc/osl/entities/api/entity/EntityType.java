package net.ornithemc.osl.entities.api.entity;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import net.ornithemc.osl.entities.impl.entity.EntityTypeImpl;

/**
 * Represents a type of entity.
 */
public interface EntityType<T extends Entity> {

	/**
	 * @return the entity class type.
	 */
	Class<? extends T> getType();

	/**
	 * @return the translation key for this entity type.
	 */
	String getTranslationKey();

	/**
	 * @return a new entity of this type.
	 */
	T create(World world);

	interface Builder<T extends Entity> {

		static <T extends Entity> Builder<T> of(Class<? extends T> type, Function<? super World, ? extends T> factory) {
			return EntityTypeImpl.Builder.of(type, factory);
		}

		EntityType<T> build();

	}
}
