package net.ornithemc.osl.blocks.api.block;

import net.minecraft.block.Block;

import net.ornithemc.osl.core.api.registry.DefaultedIdRegistry;
import net.ornithemc.osl.core.api.registry.SimpleIdRegistry;

public interface BlockExtension {

	SimpleIdRegistry<Block> REGISTRY = new DefaultedIdRegistry<>("air");

	/**
	 * @return whether this block is air.
	 */
	boolean isAir();

	/**
	 * @return whether this block is the same as the given block.
	 */
	boolean is(Block block);

}
