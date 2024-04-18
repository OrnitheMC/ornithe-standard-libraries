package net.ornithemc.osl.config.api.serdes.config.option;

import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.config.api.config.option.BlockPosOption;

public class MinecraftJsonOptionSerializers {

	public static final JsonOptionSerializer<BlockPosOption>   BLOCK_POS  = JsonOptionSerializers.register(BlockPosOption.class  , BlockPos.class);

}
