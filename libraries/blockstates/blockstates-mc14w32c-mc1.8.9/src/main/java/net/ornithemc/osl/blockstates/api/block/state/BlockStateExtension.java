package net.ornithemc.osl.blockstates.api.block.state;

import net.minecraft.block.Block;

public interface BlockStateExtension extends BlockProperties, BlockBehaviors {

	boolean is(Block block);

}
