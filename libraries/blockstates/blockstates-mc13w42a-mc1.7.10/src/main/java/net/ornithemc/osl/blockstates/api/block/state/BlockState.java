package net.ornithemc.osl.blockstates.api.block.state;

import net.minecraft.block.Block;

import net.ornithemc.osl.blockstates.api.state.State;

public interface BlockState extends State<Block, BlockState>, BlockProperties, BlockBehaviors {

	Block getBlock();

}
