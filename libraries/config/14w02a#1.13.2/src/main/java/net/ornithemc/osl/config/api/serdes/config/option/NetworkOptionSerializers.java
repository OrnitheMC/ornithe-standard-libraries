package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.Identifier;
import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.config.api.config.option.BlockPosOption;
import net.ornithemc.osl.config.api.config.option.BooleanOption;
import net.ornithemc.osl.config.api.config.option.FloatOption;
import net.ornithemc.osl.config.api.config.option.IdentifierOption;
import net.ornithemc.osl.config.api.config.option.IntegerOption;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.StringOption;
import net.ornithemc.osl.config.api.config.option.UuidOption;
import net.ornithemc.osl.config.api.serdes.MinecraftSerializerTypes;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class NetworkOptionSerializers {

	private static final Registry<Class<? extends Option>, NetworkOptionSerializer<? extends Option>> REGISTRY = OptionSerializers.register(MinecraftSerializerTypes.NETWORK, "network");

	private static final String VALUE = "value";

	public static final NetworkOptionSerializer<BooleanOption> BOOLEAN = register(BooleanOption.class, new NetworkOptionSerializer<BooleanOption>() {

		@Override
		public void serialize(BooleanOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putBoolean(VALUE, option.get());
		}

		@Override
		public void deserialize(BooleanOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(nbt.getBoolean(VALUE));
		}
	});
	public static final NetworkOptionSerializer<FloatOption> FLOAT = register(FloatOption.class, new NetworkOptionSerializer<FloatOption>() {

		@Override
		public void serialize(FloatOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putFloat(VALUE, option.get());
		}

		@Override
		public void deserialize(FloatOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(nbt.getFloat(VALUE));
		}
	});
	public static final NetworkOptionSerializer<IntegerOption> INTEGER = register(IntegerOption.class, new NetworkOptionSerializer<IntegerOption>() {

		@Override
		public void serialize(IntegerOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putInt(VALUE, option.get());
		}

		@Override
		public void deserialize(IntegerOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(nbt.getInt(VALUE));
		}
	});
	public static final NetworkOptionSerializer<StringOption> STRING = register(StringOption.class, new NetworkOptionSerializer<StringOption>() {

		@Override
		public void serialize(StringOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putString(VALUE, option.get());
		}

		@Override
		public void deserialize(StringOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(nbt.getString(VALUE));
		}
	});
	public static final NetworkOptionSerializer<UuidOption> UUID = register(UuidOption.class, new NetworkOptionSerializer<UuidOption>() {

		@Override
		public void serialize(UuidOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putUuid(VALUE, option.get());
		}

		@Override
		public void deserialize(UuidOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(nbt.getUuid(VALUE));
		}
	});

	public static final NetworkOptionSerializer<IdentifierOption> IDENTIFIER = register(IdentifierOption.class, new NetworkOptionSerializer<IdentifierOption>() {

		@Override
		public void serialize(IdentifierOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putString(VALUE, option.get().toString());
		}

		@Override
		public void deserialize(IdentifierOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(new Identifier(nbt.getString(VALUE)));
		}
	});
	public static final NetworkOptionSerializer<BlockPosOption> BLOCK_POS = register(BlockPosOption.class, new NetworkOptionSerializer<BlockPosOption>() {

		@Override
		public void serialize(BlockPosOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putLong(VALUE, option.get().toLong());
		}

		@Override
		public void deserialize(BlockPosOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(BlockPos.fromLong(nbt.getLong(VALUE)));
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
