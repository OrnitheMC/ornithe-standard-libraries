package net.ornithemc.osl.blockstates.api.block;

import net.minecraft.block.Block;

import net.ornithemc.osl.blockstates.impl.block.AirBlock;

public final class Blocks {

	/*
	 * We cannot add this to the BlockExtension interface because
	 * the Block.BY_ID array must exist when blocks are created.
	 * Other mods (like StAPI) may also add air, only one can exist!
	 */
	public static final Block AIR = Block.BY_ID[0] != null ? Block.BY_ID[0] : new AirBlock(0);

}
