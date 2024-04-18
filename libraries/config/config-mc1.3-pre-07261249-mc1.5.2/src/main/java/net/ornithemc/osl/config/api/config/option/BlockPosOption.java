package net.ornithemc.osl.config.api.config.option;

import java.util.function.Predicate;

import net.minecraft.util.math.BlockPos;

public class BlockPosOption extends BaseOption<BlockPos> {

	public BlockPosOption(String name, String description, BlockPos defaultValue) {
		super(name, description, defaultValue);
	}

	public BlockPosOption(String name, String description, BlockPos defaultValue, Predicate<BlockPos> validator) {
		super(name, description, defaultValue, validator);
	}
}
