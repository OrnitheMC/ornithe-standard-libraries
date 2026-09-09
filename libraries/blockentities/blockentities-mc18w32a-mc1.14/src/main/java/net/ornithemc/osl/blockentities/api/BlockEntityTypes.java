package net.ornithemc.osl.blockentities.api;

import java.util.function.Supplier;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

/**
 * Utilities for constructing and modifying block entity types.
 */
public final class BlockEntityTypes {

	/**
	 * @return a new block entity type builder with the given block entity factory.
	 */
	public static <T extends BlockEntity> BlockEntityType.Builder<T> builder(Supplier<? extends T> factory) {
		return BlockEntityType.Builder.of(factory);
	}
}
