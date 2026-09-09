package net.ornithemc.osl.entities.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.living.mob.MobCategory;

/**
 * Utilities for constructing and modifying entity types.
 */
public final class EntityTypes {

	/**
	 * @return a new entity type builder with the given entity factory and mob category.
	 */
	public static <T extends Entity> EntityType.Builder<T> builder(EntityType.C_34376702<T> factory, MobCategory category) {
		return EntityType.Builder.of(factory, category);
	}
}
