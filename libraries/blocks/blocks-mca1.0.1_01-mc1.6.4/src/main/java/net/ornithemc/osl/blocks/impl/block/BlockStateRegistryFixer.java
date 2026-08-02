package net.ornithemc.osl.blocks.impl.block;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.blockstates.api.block.state.BlockState;
import net.ornithemc.osl.registries.api.registry.sync.IdFixer;

public class BlockStateRegistryFixer implements IdFixer {

	@Override
	public void apply() {
		Block.STATE_REGISTRY.clear();

		for (Block block : BlockRegistry.REGISTRY) {
			int blockId = BlockRegistry.getId(block);

			for (BlockState state : block.stateDefinition().all()) {
				int metadata = block.getMetadataFromState(state);
				int stateId = blockId << 4 | metadata;

				Block.STATE_REGISTRY.put(state, stateId);
			}
		}
	}
}
