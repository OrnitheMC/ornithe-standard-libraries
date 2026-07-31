package net.ornithemc.osl.blockentities.impl;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.resource.Identifier;
import net.minecraft.util.registry.IdRegistry;

/**
 * The block entity type registry is created and populated in BlockEntity's class initializer.
 * This breaks OSL's usual pattern of registering the registry in the API entrypoint but
 * triggering registry population during bootstrapping, since OSL has to wrap the Vanilla
 * registry which is not possible without triggering BlockEntity class load and registry
 * population.
 * The solution: move the Vanilla registry to another class and replace the IdRegistry
 * reference in BlockEntity with this one.
 */
public final class BlockEntityTypeIdRegistry {

	public static final IdRegistry<Identifier, Class<? extends BlockEntity>> REGISTRY = new IdRegistry<>();

}
