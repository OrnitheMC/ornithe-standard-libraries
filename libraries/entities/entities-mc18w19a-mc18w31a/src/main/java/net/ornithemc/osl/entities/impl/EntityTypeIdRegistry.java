package net.ornithemc.osl.entities.impl;

import net.minecraft.entity.EntityType;
import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.IdRegistry;

/**
 * The entity type registry is created and populated in EntityType's class initializer.
 * This breaks OSL's usual pattern of registering the registry in the API entrypoint but
 * triggering registry population during bootstrapping, since OSL has to wrap the Vanilla
 * registry which is not possible without triggering EntityType class load and registry
 * population.
 * The solution: move the Vanilla registry to another class and replace the IdRegistry
 * reference in EntityType with this one.
 */
public final class EntityTypeIdRegistry {

	public static final IdRegistry<Identifier, EntityType<?>> REGISTRY = new IdRegistry<>();

}
