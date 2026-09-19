package net.ornithemc.osl.blockentities.api.blockentity;

import java.util.function.Supplier;

import net.minecraft.block.entity.BlockEntity;

import net.ornithemc.osl.blockentities.impl.blockentity.BlockEntityTypeImpl;

/**
 * Represents a type of block entity.
 */
public interface BlockEntityType<T extends BlockEntity> {

	/**
	 * @return the block entity class type.
	 */
	Class<? extends T> getType();

	/**
	 * @return a new block entity of this type.
	 */
	T create();

	interface Builder<T extends BlockEntity> {

		static <T extends BlockEntity> Builder<T> of(Class<? extends T> type, Supplier<? extends T> factory) {
			return BlockEntityTypeImpl.Builder.of(type, factory);
		}

		BlockEntityType<T> build();

	}
}
