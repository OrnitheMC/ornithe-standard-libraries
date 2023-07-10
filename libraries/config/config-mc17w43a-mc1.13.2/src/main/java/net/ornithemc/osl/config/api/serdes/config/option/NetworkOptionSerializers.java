package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;
import java.nio.file.Paths;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.config.api.config.option.BlockPosOption;
import net.ornithemc.osl.config.api.config.option.BooleanOption;
import net.ornithemc.osl.config.api.config.option.FloatOption;
import net.ornithemc.osl.config.api.config.option.IdentifierOption;
import net.ornithemc.osl.config.api.config.option.IntegerOption;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.PathOption;
import net.ornithemc.osl.config.api.config.option.StringOption;
import net.ornithemc.osl.config.api.config.option.UuidOption;
import net.ornithemc.osl.config.api.serdes.MinecraftSerializerTypes;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class NetworkOptionSerializers {

	private static final Registry<Class<? extends Option>, NetworkOptionSerializer<? extends Option>> REGISTRY = OptionSerializers.register(MinecraftSerializerTypes.NETWORK, "network");

	public static final NetworkOptionSerializer<BooleanOption> BOOLEAN = register(BooleanOption.class, new NetworkOptionSerializer<BooleanOption>() {

		@Override
		public void serialize(BooleanOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			buffer.writeBoolean(option.get());
		}

		@Override
		public void deserialize(BooleanOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			option.set(buffer.readBoolean());
		}
	});
	public static final NetworkOptionSerializer<FloatOption> FLOAT = register(FloatOption.class, new NetworkOptionSerializer<FloatOption>() {

		@Override
		public void serialize(FloatOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			buffer.writeFloat(option.get());
		}

		@Override
		public void deserialize(FloatOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			option.set(buffer.readFloat());
		}
	});
	public static final NetworkOptionSerializer<IntegerOption> INTEGER = register(IntegerOption.class, new NetworkOptionSerializer<IntegerOption>() {

		@Override
		public void serialize(IntegerOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			buffer.writeInt(option.get());
		}

		@Override
		public void deserialize(IntegerOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			option.set(buffer.readInt());
		}
	});
	public static final NetworkOptionSerializer<PathOption> PATH = register(PathOption.class, new NetworkOptionSerializer<PathOption>() {

		@Override
		public void serialize(PathOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			buffer.writeString(option.get().toAbsolutePath().toString());
		}

		@Override
		public void deserialize(PathOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			option.set(Paths.get(buffer.readString(32767)));
		}
	});
	public static final NetworkOptionSerializer<StringOption> STRING = register(StringOption.class, new NetworkOptionSerializer<StringOption>() {

		@Override
		public void serialize(StringOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			buffer.writeString(option.get());
		}

		@Override
		public void deserialize(StringOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			option.set(buffer.readString(32767));
		}
	});
	public static final NetworkOptionSerializer<UuidOption> UUID = register(UuidOption.class, new NetworkOptionSerializer<UuidOption>() {

		@Override
		public void serialize(UuidOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			buffer.writeUuid(option.get());
		}

		@Override
		public void deserialize(UuidOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			option.set(buffer.readUuid());
		}
	});

	public static final NetworkOptionSerializer<IdentifierOption> IDENTIFIER = register(IdentifierOption.class, new NetworkOptionSerializer<IdentifierOption>() {

		@Override
		public void serialize(IdentifierOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			buffer.writeString(option.get().toString());
		}

		@Override
		public void deserialize(IdentifierOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			option.set(new Identifier(buffer.readString(32767)));
		}
	});
	public static final NetworkOptionSerializer<BlockPosOption> BLOCK_POS = register(BlockPosOption.class, new NetworkOptionSerializer<BlockPosOption>() {

		@Override
		public void serialize(BlockPosOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			buffer.writeBlockPos(option.get());
		}

		@Override
		public void deserialize(BlockPosOption option, SerializationSettings settings, PacketByteBuf buffer) throws IOException {
			option.set(buffer.readBlockPos());
		}
	});

	public static <O extends Option, S extends NetworkOptionSerializer<O>> S register(Class<O> optionType, S serializer) {
		return Registries.registerMapping(REGISTRY, optionType, serializer);
	}

	@SuppressWarnings("unchecked")
	public static <O extends Option> NetworkOptionSerializer<O> get(Class<? extends Option> optionType) {
		return (NetworkOptionSerializer<O>)REGISTRY.get(optionType);
	}
}
