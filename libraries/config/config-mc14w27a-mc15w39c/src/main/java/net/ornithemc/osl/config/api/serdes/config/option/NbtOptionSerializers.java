package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.Identifier;
import net.minecraft.util.math.BlockPos;

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

public class NbtOptionSerializers {

	private static final Registry<Class<? extends Option>, NbtOptionSerializer<? extends Option>> REGISTRY = OptionSerializers.register(MinecraftSerializerTypes.NBT, "nbt");

	public static final NbtOptionSerializer<BooleanOption> BOOLEAN = register(BooleanOption.class, new NbtOptionSerializer<BooleanOption>() {

		@Override
		public void serialize(BooleanOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putBoolean(option.getName(), option.get());
		}

		@Override
		public void deserialize(BooleanOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(nbt.getBoolean(option.getName()));
		}
	});
	public static final NbtOptionSerializer<FloatOption> FLOAT = register(FloatOption.class, new NbtOptionSerializer<FloatOption>() {

		@Override
		public void serialize(FloatOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putFloat(option.getName(), option.get());
		}

		@Override
		public void deserialize(FloatOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(nbt.getFloat(option.getName()));
		}
	});
	public static final NbtOptionSerializer<IntegerOption> INTEGER = register(IntegerOption.class, new NbtOptionSerializer<IntegerOption>() {

		@Override
		public void serialize(IntegerOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putInt(option.getName(), option.get());
		}

		@Override
		public void deserialize(IntegerOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(nbt.getInt(option.getName()));
		}
	});
	public static final NbtOptionSerializer<PathOption> PATH = register(PathOption.class, new NbtOptionSerializer<PathOption>() {

		@Override
		public void serialize(PathOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putString(option.getName(), option.get().toAbsolutePath().toString());
		}

		@Override
		public void deserialize(PathOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(Paths.get(nbt.getString(option.getName())));
		}
	});
	public static final NbtOptionSerializer<StringOption> STRING = register(StringOption.class, new NbtOptionSerializer<StringOption>() {

		@Override
		public void serialize(StringOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putString(option.getName(), option.get());
		}

		@Override
		public void deserialize(StringOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(nbt.getString(option.getName()));
		}
	});
	public static final NbtOptionSerializer<UuidOption> UUID = register(UuidOption.class, new NbtOptionSerializer<UuidOption>() {

		@Override
		public void serialize(UuidOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			String name = option.getName();
			UUID value = option.get();

			nbt.putLong(name + "Most", value.getMostSignificantBits());
			nbt.putLong(name + "Least", value.getLeastSignificantBits());
		}

		@Override
		public void deserialize(UuidOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			String name = option.getName();

			long most = nbt.getLong(name + "Most");
			long least = nbt.getLong(name + "Least");

			option.set(new UUID(most, least));
		}
	});

	public static final NbtOptionSerializer<IdentifierOption> IDENTIFIER = register(IdentifierOption.class, new NbtOptionSerializer<IdentifierOption>() {

		@Override
		public void serialize(IdentifierOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			nbt.putString(option.getName(), option.get().toString());
		}

		@Override
		public void deserialize(IdentifierOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			option.set(new Identifier(nbt.getString(option.getName())));
		}
	});
	public static final NbtOptionSerializer<BlockPosOption> BLOCK_POS = register(BlockPosOption.class, new NbtOptionSerializer<BlockPosOption>() {

		private static final String X = "x";
		private static final String Y = "y";
		private static final String Z = "z";

		@Override
		public void serialize(BlockPosOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			NbtCompound value = new NbtCompound();
			value.putInt(X, option.get().getX());
			value.putInt(Y, option.get().getY());
			value.putInt(Z, option.get().getZ());
			nbt.put(option.getName(), value);
		}

		@Override
		public void deserialize(BlockPosOption option, SerializationSettings settings, NbtCompound nbt) throws IOException {
			NbtCompound value = nbt.getCompound(option.getName());
			int x = value.getInt(X);
			int y = value.getInt(Y);
			int z = value.getInt(Z);
			option.set(new BlockPos(x, y, z));
		}
	});

	public static <O extends Option, S extends NbtOptionSerializer<O>> S register(Class<O> optionType, S serializer) {
		return Registries.registerMapping(REGISTRY, optionType, serializer);
	}

	@SuppressWarnings("unchecked")
	public static <O extends Option> NbtOptionSerializer<O> get(Class<? extends Option> optionType) {
		return (NbtOptionSerializer<O>)REGISTRY.get(optionType);
	}
}
