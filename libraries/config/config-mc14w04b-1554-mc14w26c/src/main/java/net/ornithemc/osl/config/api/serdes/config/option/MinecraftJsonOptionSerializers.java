package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.config.api.config.option.BlockPosOption;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.core.api.json.JsonFile;

public class MinecraftJsonOptionSerializers {

	public static final JsonOptionSerializer<BlockPosOption> BLOCK_POS = JsonOptionSerializers.register(BlockPosOption.class, new JsonOptionSerializer<BlockPosOption>() {

		private static final String X = "x";
		private static final String Y = "y";
		private static final String Z = "z";

		@Override
		public void serialize(BlockPosOption option, SerializationSettings settings, JsonFile json) throws IOException {
			json.writeObject(_json -> {
				BlockPos pos = option.get();

				json.writeNumber(X, pos.getX());
				json.writeNumber(Y, pos.getY());
				json.writeNumber(Z, pos.getZ());
			});
		}

		@Override
		public void deserialize(BlockPosOption option, SerializationSettings settings, JsonFile json) throws IOException {
			json.readObject(_json -> {
				int x = json.readNumber(X).intValue();
				int y = json.readNumber(Y).intValue();
				int z = json.readNumber(Z).intValue();

				option.set(new BlockPos(x, y, z));
			});
		}
	});
}
