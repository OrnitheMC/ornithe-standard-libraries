package net.ornithemc.osl.blocks.impl.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.registries.api.registry.sync.IdFixer;
import net.ornithemc.osl.registries.impl.access.Clearable;

public class BlockStateRegistryFixer implements IdFixer {

	@Override
	public void apply() {
		Clearable.clear(Block.STATE_REGISTRY);

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
