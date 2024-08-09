package net.ornithemc.osl.config.api.serdes.config.option;

import java.io.IOException;
import java.nio.file.Path;

import net.minecraft.nbt.NbtElement;
import net.minecraft.resource.Identifier;
import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.config.api.config.option.BaseOption;
import net.ornithemc.osl.config.api.config.option.BlockPosOption;
import net.ornithemc.osl.config.api.config.option.BooleanOption;
import net.ornithemc.osl.config.api.config.option.ByteOption;
import net.ornithemc.osl.config.api.config.option.CharacterOption;
import net.ornithemc.osl.config.api.config.option.DoubleOption;
import net.ornithemc.osl.config.api.config.option.FloatOption;
import net.ornithemc.osl.config.api.config.option.IdentifierOption;
import net.ornithemc.osl.config.api.config.option.IntegerOption;
import net.ornithemc.osl.config.api.config.option.ListOption;
import net.ornithemc.osl.config.api.config.option.LongOption;
import net.ornithemc.osl.config.api.config.option.MapOption;
import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.option.PathOption;
import net.ornithemc.osl.config.api.config.option.ShortOption;
import net.ornithemc.osl.config.api.config.option.StringOption;
import net.ornithemc.osl.config.api.config.option.UuidOption;
import net.ornithemc.osl.config.api.serdes.NbtSerializers;
import net.ornithemc.osl.config.api.serdes.MinecraftSerializerTypes;
import net.ornithemc.osl.config.api.serdes.SerializationSettings;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class NbtOptionSerializers {

	private static final Registry<Class<? extends Option>, NbtOptionSerializer<? extends Option>> REGISTRY = OptionSerializers.register(MinecraftSerializerTypes.NBT, "nbt");

	public static final NbtOptionSerializer<BooleanOption>    BOOLEAN    = register(BooleanOption.class   , Boolean.class);
	public static final NbtOptionSerializer<ByteOption>       BYTE       = register(ByteOption.class      , Byte.class);
	public static final NbtOptionSerializer<DoubleOption>     DOUBLE     = register(DoubleOption.class    , Double.class);
	public static final NbtOptionSerializer<FloatOption>      FLOAT      = register(FloatOption.class     , Float.class);
	public static final NbtOptionSerializer<IntegerOption>    INTEGER    = register(IntegerOption.class   , Integer.class);
	public static final NbtOptionSerializer<LongOption>       LONG       = register(LongOption.class      , Long.class);
	public static final NbtOptionSerializer<ShortOption>      SHORT      = register(ShortOption.class     , Short.class);
	public static final NbtOptionSerializer<CharacterOption>  CHARACTER  = register(CharacterOption.class , Character.class);
	public static final NbtOptionSerializer<StringOption>     STRING     = register(StringOption.class    , String.class);
	public static final NbtOptionSerializer<PathOption>       PATH       = register(PathOption.class      , Path.class);
	public static final NbtOptionSerializer<UuidOption>       UUID       = register(UuidOption.class      , java.util.UUID.class);
	public static final NbtOptionSerializer<IdentifierOption> IDENTIFIER = register(IdentifierOption.class, Identifier.class);
	public static final NbtOptionSerializer<BlockPosOption>   BLOCK_POS  = register(BlockPosOption.class  , BlockPos.class);

	@SuppressWarnings("rawtypes")
	public static final NbtOptionSerializer<ListOption> LIST = register(ListOption.class, new NbtOptionSerializer<ListOption>() {

		@Override
		@SuppressWarnings("unchecked")
		public NbtElement serialize(ListOption option, SerializationSettings settings) throws IOException {
			return NbtSerializers.Lists.serialize(option);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void deserialize(ListOption option, SerializationSettings settings, NbtElement nbt) throws IOException {
			NbtSerializers.Lists.deserialize(option, nbt);
		}
	});
	@SuppressWarnings("rawtypes")
	public static final NbtOptionSerializer<MapOption> MAP = register(MapOption.class, new NbtOptionSerializer<MapOption>() {

		@Override
		@SuppressWarnings("unchecked")
		public NbtElement serialize(MapOption option, SerializationSettings settings) throws IOException {
			return NbtSerializers.Maps.serialize(option);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void deserialize(MapOption option, SerializationSettings settings, NbtElement nbt) throws IOException {
			NbtSerializers.Maps.deserialize(option, nbt);
		}
	});

	public static <T, O extends BaseOption<T>> NbtOptionSerializer<O> register(Class<O> optionType, Class<T> valueType) {
		return register(optionType, new NbtOptionSerializer<O>() {

			@Override
			public NbtElement serialize(O option, SerializationSettings settings) throws IOException {
				return NbtSerializers.get(valueType).serialize(option.get());
			}

			@Override
			public void deserialize(O option, SerializationSettings settings, NbtElement nbt) throws IOException {
				option.set(NbtSerializers.get(valueType).deserialize(nbt));
			}
		});
	}

	public static <O extends Option, S extends NbtOptionSerializer<O>> S register(Class<O> optionType, S serializer) {
		return Registries.registerMapping(REGISTRY, optionType, serializer);
	}

	public static <O extends Option> NbtOptionSerializer<O> get(Class<? extends Option> optionType) {
		return Registries.getMapping(REGISTRY, optionType);
	}
}
