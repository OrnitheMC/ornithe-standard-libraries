package net.ornithemc.osl.blocks.api.block;

import net.minecraft.block.Block;

import net.ornithemc.osl.blocks.impl.BlockRegistryImpl;
import net.ornithemc.osl.registries.api.registry.DefaultedRegistry;

public interface BlockExtension {

	DefaultedRegistry<Block> REGISTRY = BlockRegistryImpl.REGISTRY;
	int AUTO_ASSIGN_ID = -172;

}
