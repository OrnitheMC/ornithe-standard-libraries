package net.ornithemc.osl.blockentities.api.blockentity;

import java.util.function.Supplier;

import net.minecraft.block.entity.BlockEntity;

/**
 * Utilities for constructing and modifying block entity types.
 */
public final class BlockEntityTypes {

	/**
	 * @return a new block entity type builder with the given block entity type class and block entity factory.
	 */
	public static <T extends BlockEntity> BlockEntityType.Builder<T> builder(Class<? extends T> type, Supplier<? extends T> factory) {
		return BlockEntityType.Builder.of(type, factory);
	}
}
