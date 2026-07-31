package net.ornithemc.osl.blockentities.api;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import net.ornithemc.osl.blockentities.impl.BlockEntityTypeRegistryImpl;

/**
 * Utilities for constructing and modifying block entity types.
 */
public final class BlockEntityTypes {

	/**
	 * @return a new block entity type builder with the given block entity factory and the given blocks.
	 */
	public static <T extends BlockEntity> BlockEntityType.Builder<T> builder(Supplier<? extends T> factory, Block... blocks) {
		return BlockEntityType.Builder.m_33563020(factory, blocks);
	}

	/**
	 * Adds the given blocks to the given block entity type.
	 * 
	 * @param type   the block entity type.
	 * @param blocks the blocks to add to the block entity type.
	 */
	public static void addBlocks(BlockEntityType<?> type, Block... blocks) {
		BlockEntityTypeRegistryImpl.addBlocks(type, blocks);
	}
}
