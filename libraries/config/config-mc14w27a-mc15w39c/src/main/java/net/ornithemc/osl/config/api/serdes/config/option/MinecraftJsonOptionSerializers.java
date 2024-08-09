package net.ornithemc.osl.config.api.serdes.config.option;

import net.minecraft.resource.Identifier;
import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.config.api.config.option.BlockPosOption;
import net.ornithemc.osl.config.api.config.option.IdentifierOption;

public class MinecraftJsonOptionSerializers {

	public static final JsonOptionSerializer<IdentifierOption> IDENTIFIER = JsonOptionSerializers.register(IdentifierOption.class, Identifier.class);
	public static final JsonOptionSerializer<BlockPosOption>   BLOCK_POS  = JsonOptionSerializers.register(BlockPosOption.class  , BlockPos.class);

}
