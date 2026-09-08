package net.ornithemc.osl.blocks.impl.block;

import net.minecraft.block.Block;

import net.ornithemc.osl.registries.api.registry.sync.IdFixer;

public class BlockIdFixer implements IdFixer {

	@Override
	public void apply() {
		for (int id = 0; id < Block.BY_ID.length; id++) {
			Block block = Block.BY_ID[id];

			if (block != null) {
				block.id = id;
			}
		}
	}
}
