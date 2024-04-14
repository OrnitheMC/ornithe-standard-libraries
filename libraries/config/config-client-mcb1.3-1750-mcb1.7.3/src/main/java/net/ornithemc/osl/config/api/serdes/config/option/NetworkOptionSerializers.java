package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;
import java.nio.file.Path;

import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.config.api.config.option.BaseOption;
import net.ornithemc.osl.config.api.config.option.BlockPosOption;
import net.ornithemc.osl.config.api.config.option.BooleanOption;
import net.ornithemc.osl.config.api.config.option.ByteOption;
import net.ornithemc.osl.config.api.config.option.CharacterOption;
import net.ornithemc.osl.config.api.config.option.DoubleOption;
import net.ornithemc.osl.config.api.config.option.FloatOption;
import net.ornithemc.osl.config.api.config.option.IntegerOption;
import net.ornithemc.osl.config.api.config.option.ListOption;
import net.ornithemc.osl.config.api.config.option.LongOption;
import net.ornithemc.osl.config.api.config.option.MapOption;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.PathOption;
import net.ornithemc.osl.config.api.config.option.ShortOption;
import net.ornithemc.osl.config.api.config.option.StringOption;
import net.ornithemc.osl.config.api.config.option.UuidOption;
import net.ornithemc.osl.config.api.serdes.MinecraftSerializerTypes;
import net.ornithemc.osl.config.api.serdes.NetworkSerializers;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.core.api.io.DataStream;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class NetworkOptionSerializers {

	private static final Registry<Class<? extends Option>, NetworkOptionSerializer<? extends Option>> REGISTRY = OptionSerializers.register(MinecraftSerializerTypes.NETWORK, "network");

	public static final NetworkOptionSerializer<BooleanOption>    BOOLEAN    = register(BooleanOption.class   , Boolean.class);
	public static final NetworkOptionSerializer<ByteOption>       BYTE       = register(ByteOption.class      , Byte.class);
	public static final NetworkOptionSerializer<DoubleOption>     DOUBLE     = register(DoubleOption.class    , Double.class);
	public static final NetworkOptionSerializer<FloatOption>      FLOAT      = register(FloatOption.class     , Float.class);
	public static final NetworkOptionSerializer<IntegerOption>    INTEGER    = register(IntegerOption.class   , Integer.class);
	public static final NetworkOptionSerializer<LongOption>       LONG       = register(LongOption.class      , Long.class);
	public static final NetworkOptionSerializer<ShortOption>      SHORT      = register(ShortOption.class     , Short.class);
	public static final NetworkOptionSerializer<CharacterOption>  CHARACTER  = register(CharacterOption.class , Character.class);
	public static final NetworkOptionSerializer<StringOption>     STRING     = register(StringOption.class    , String.class);
	public static final NetworkOptionSerializer<PathOption>       PATH       = register(PathOption.class      , Path.class);
	public static final NetworkOptionSerializer<UuidOption>       UUID       = register(UuidOption.class      , java.util.UUID.class);
	public static final NetworkOptionSerializer<BlockPosOption>   BLOCK_POS  = register(BlockPosOption.class  , BlockPos.class);

	@SuppressWarnings("rawtypes")
	public static final NetworkOptionSerializer<ListOption> LIST = register(ListOption.class, new NetworkOptionSerializer<ListOption>() {

		@Override
		@SuppressWarnings("unchecked")
		public void serialize(ListOption option, SerializationSettings settings, DataStream data) throws IOException {
			NetworkSerializers.Lists.serialize(option, data);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void deserialize(ListOption option, SerializationSettings settings, DataStream data) throws IOException {
			NetworkSerializers.Lists.deserialize(option, data);
		}
	});
	@SuppressWarnings("rawtypes")
	public static final NetworkOptionSerializer<MapOption> MAP = register(MapOption.class, new NetworkOptionSerializer<MapOption>() {

		@Override
		@SuppressWarnings("unchecked")
		public void serialize(MapOption option, SerializationSettings settings, DataStream data) throws IOException {
			NetworkSerializers.Maps.serialize(option, data);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void deserialize(MapOption option, SerializationSettings settings, DataStream data) throws IOException {
			NetworkSerializers.Maps.deserialize(option, data);
		}
	});

	public static <T, O extends BaseOption<T>> NetworkOptionSerializer<O> register(Class<O> optionType, Class<T> valueType) {
		return register(optionType, new NetworkOptionSerializer<O>() {

			@Override
			public void serialize(O option, SerializationSettings settings, DataStream data) throws IOException {
				NetworkSerializers.get(valueType).serialize(option.get(), data);
			}

			@Override
			public void deserialize(O option, SerializationSettings settings, DataStream data) throws IOException {
				option.set(NetworkSerializers.get(valueType).deserialize(data));
			}
		});
	}

	public static <O extends Option, S extends NetworkOptionSerializer<O>> S register(Class<O> optionType, S serializer) {
		return Registries.registerMapping(REGISTRY, optionType, serializer);
	}

	public static <O extends Option> NetworkOptionSerializer<O> get(Class<? extends Option> optionType) {
		return Registries.getMapping(REGISTRY, optionType);
	}
}
