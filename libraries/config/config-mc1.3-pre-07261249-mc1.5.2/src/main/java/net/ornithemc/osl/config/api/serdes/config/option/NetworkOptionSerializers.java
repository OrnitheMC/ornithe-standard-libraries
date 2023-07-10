package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import net.ornithemc.osl.config.api.config.option.BooleanOption;
import net.ornithemc.osl.config.api.config.option.FloatOption;
import net.ornithemc.osl.config.api.config.option.IntegerOption;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.PathOption;
import net.ornithemc.osl.config.api.config.option.StringOption;
import net.ornithemc.osl.config.api.config.option.UuidOption;
import net.ornithemc.osl.config.api.serdes.MinecraftSerializerTypes;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.core.api.io.DataStream;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class NetworkOptionSerializers {

	private static final Registry<Class<? extends Option>, NetworkOptionSerializer<? extends Option>> REGISTRY = OptionSerializers.register(MinecraftSerializerTypes.NETWORK, "network");

	public static final NetworkOptionSerializer<BooleanOption> BOOLEAN = register(BooleanOption.class, new NetworkOptionSerializer<BooleanOption>() {

		@Override
		public void serialize(BooleanOption option, SerializationSettings settings, DataStream ds) throws IOException {
			ds.writeBoolean(option.get());
		}

		@Override
		public void deserialize(BooleanOption option, SerializationSettings settings, DataStream ds) throws IOException {
			option.set(ds.readBoolean());
		}
	});
	public static final NetworkOptionSerializer<FloatOption> FLOAT = register(FloatOption.class, new NetworkOptionSerializer<FloatOption>() {

		@Override
		public void serialize(FloatOption option, SerializationSettings settings, DataStream ds) throws IOException {
			ds.writeFloat(option.get());
		}

		@Override
		public void deserialize(FloatOption option, SerializationSettings settings, DataStream ds) throws IOException {
			option.set(ds.readFloat());
		}
	});
	public static final NetworkOptionSerializer<IntegerOption> INTEGER = register(IntegerOption.class, new NetworkOptionSerializer<IntegerOption>() {

		@Override
		public void serialize(IntegerOption option, SerializationSettings settings, DataStream ds) throws IOException {
			ds.writeInt(option.get());
		}

		@Override
		public void deserialize(IntegerOption option, SerializationSettings settings, DataStream ds) throws IOException {
			option.set(ds.readInt());
		}
	});
	public static final NetworkOptionSerializer<PathOption> PATH = register(PathOption.class, new NetworkOptionSerializer<PathOption>() {

		@Override
		public void serialize(PathOption option, SerializationSettings settings, DataStream ds) throws IOException {
			ds.writeUTF(option.get().toAbsolutePath().toString());
		}

		@Override
		public void deserialize(PathOption option, SerializationSettings settings, DataStream ds) throws IOException {
			option.set(Paths.get(ds.readUTF()));
		}
	});
	public static final NetworkOptionSerializer<StringOption> STRING = register(StringOption.class, new NetworkOptionSerializer<StringOption>() {

		@Override
		public void serialize(StringOption option, SerializationSettings settings, DataStream ds) throws IOException {
			ds.writeUTF(option.get());
		}

		@Override
		public void deserialize(StringOption option, SerializationSettings settings, DataStream ds) throws IOException {
			option.set(ds.readUTF());
		}
	});
	public static final NetworkOptionSerializer<UuidOption> UUID = register(UuidOption.class, new NetworkOptionSerializer<UuidOption>() {

		@Override
		public void serialize(UuidOption option, SerializationSettings settings, DataStream ds) throws IOException {
			UUID value = option.get();

			ds.writeLong(value.getMostSignificantBits());
			ds.writeLong(value.getLeastSignificantBits());
		}

		@Override
		public void deserialize(UuidOption option, SerializationSettings settings, DataStream ds) throws IOException {
			long most = ds.readLong();
			long least=  ds.readLong();

			option.set(new UUID(most, least));
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
