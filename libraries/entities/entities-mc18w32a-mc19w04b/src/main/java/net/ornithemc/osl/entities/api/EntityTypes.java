package net.ornithemc.osl.entities.api;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Utilities for constructing and modifying entity types.
 */
public final class EntityTypes {

	/**
	 * @return a new entity type builder with the given entity type class and entity factory.
	 */
	public static <T extends Entity> EntityType.Builder<T> builder(Class<? extends T> type, Function<? super World, ? extends T> factory) {
		return EntityType.Builder.of(type, factory);
	}
}
