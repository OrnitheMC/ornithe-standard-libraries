package net.ornithemc.osl.config.api.serdes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.config.api.config.option.ListOption;
import net.ornithemc.osl.config.api.config.option.MapOption;
import net.ornithemc.osl.config.impl.interfaces.mixin.INbtCompound;
import net.ornithemc.osl.config.impl.interfaces.mixin.INbtList;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class NbtSerializers {

	private static final Registry<Class<?>, NbtSerializer<?>> REGISTRY = Serializers.register(MinecraftSerializerTypes.NBT, "nbt");

	public static final NbtSerializer<Boolean> BOOLEAN = register(Boolean.class, new NbtSerializer<Boolean>() {

		@Override
		public NbtElement serialize(Boolean value) throws IOException {
			return BYTE.serialize(value ? (byte)0b1 : (byte)0b0);
		}

		@Override
		public Boolean deserialize(NbtElement nbt) throws IOException {
			return BYTE.deserialize(nbt) == 0b1 ? Boolean.TRUE : Boolean.FALSE;
		}
	});
	public static final NbtSerializer<Byte> BYTE = register(Byte.class, new NbtSerializer<Byte>() {

		@Override
		public NbtElement serialize(Byte value) throws IOException {
			return new NbtByte(value);
		}

		@Override
		public Byte deserialize(NbtElement nbt) throws IOException {
			return NbtType.BYTE.cast(nbt).value;
		}
	});
	public static final NbtSerializer<Double> DOUBLE = register(Double.class, new NbtSerializer<Double>() {

		@Override
		public NbtElement serialize(Double value) throws IOException {
			return new NbtDouble(value);
		}

		@Override
		public Double deserialize(NbtElement nbt) throws IOException {
			return NbtType.DOUBLE.cast(nbt).value;
		}
	});
	public static final NbtSerializer<Float> FLOAT = register(Float.class, new NbtSerializer<Float>() {

		@Override
		public NbtElement serialize(Float value) throws IOException {
			return new NbtFloat(value);
		}

		@Override
		public Float deserialize(NbtElement nbt) throws IOException {
			return NbtType.FLOAT.cast(nbt).value;
		}
	});
	public static final NbtSerializer<Integer> INTEGER = register(Integer.class, new NbtSerializer<Integer>() {

		@Override
		public NbtElement serialize(Integer value) throws IOException {
			return new NbtInt(value);
		}

		@Override
		public Integer deserialize(NbtElement nbt) throws IOException {
			return NbtType.INT.cast(nbt).value;
		}
	});
	public static final NbtSerializer<Long> LONG = register(Long.class, new NbtSerializer<Long>() {

		@Override
		public NbtElement serialize(Long value) throws IOException {
			return new NbtLong(value);
		}

		@Override
		public Long deserialize(NbtElement nbt) throws IOException {
			return NbtType.LONG.cast(nbt).value;
		}
	});
	public static final NbtSerializer<Short> SHORT = register(Short.class, new NbtSerializer<Short>() {

		@Override
		public NbtElement serialize(Short value) throws IOException {
			return new NbtShort(value);
		}

		@Override
		public Short deserialize(NbtElement nbt) throws IOException {
			return NbtType.SHORT.cast(nbt).value;
		}
	});
	public static final NbtSerializer<Character> CHARACTER = register(Character.class, new NbtSerializer<Character>() {

		@Override
		public NbtElement serialize(Character value) throws IOException {
			return STRING.serialize(value.toString());
		}

		@Override
		public Character deserialize(NbtElement nbt) throws IOException {
			return STRING.deserialize(nbt).charAt(0);
		}
	});
	public static final NbtSerializer<String> STRING = register(String.class, new NbtSerializer<String>() {

		@Override
		public NbtElement serialize(String value) throws IOException {
			return new NbtString(value);
		}

		@Override
		public String deserialize(NbtElement nbt) throws IOException {
			return NbtType.STRING.cast(nbt).value;
		}
	});
	public static final NbtSerializer<Path> PATH = register(Path.class, new NbtSerializer<Path>() {

		@Override
		public NbtElement serialize(Path value) throws IOException {
			return STRING.serialize(value.toAbsolutePath().toString());
		}

		@Override
		public Path deserialize(NbtElement nbt) throws IOException {
			return Paths.get(STRING.deserialize(nbt));
		}
	});
	public static final NbtSerializer<UUID> UUID = register(UUID.class, new NbtSerializer<UUID>() {

		@Override
		public NbtElement serialize(UUID value) throws IOException {
			return STRING.serialize(value.toString());
		}

		@Override
		public UUID deserialize(NbtElement nbt) throws IOException {
			return java.util.UUID.fromString(STRING.deserialize(nbt));
		}
	});
	public static final NbtSerializer<BlockPos> BLOCK_POS = register(BlockPos.class, new NbtSerializer<BlockPos>() {

		private static final String X = "x";
		private static final String Y = "y";
		private static final String Z = "z";

		@Override
		public NbtElement serialize(BlockPos value) throws IOException {
			NbtCompound nbt = new NbtCompound();

			nbt.putInt(X, value.x);
			nbt.putInt(Y, value.y);
			nbt.putInt(Z, value.z);

			return nbt;
		}

		@Override
		public BlockPos deserialize(NbtElement nbt) throws IOException {
			NbtCompound nbtCompound = NbtType.COMPOUND.cast(nbt);

			int x = nbtCompound.getInt(X);
			int y = nbtCompound.getInt(Y);
			int z = nbtCompound.getInt(Z);

			return new BlockPos(x, y, z);
		}
	});

	public static class Lists {

		public static <T> NbtElement serialize(ListOption<T> option) throws IOException {
			return serialize(option.get(), option.getElementType());
		}

		public static <T> NbtElement serialize(List<T> list, Class<T> type) throws IOException {
			NbtList nbtList = new NbtList();
			NbtSerializer<T> serializer = NbtSerializers.get(type);

			for (int i = 0; i < list.size(); i++) {
				nbtList.addElement(serializer.serialize(list.get(i)));
			}

			return nbtList;
		}

		public static <T> void deserialize(ListOption<T> option, NbtElement nbt) throws IOException {
			option.modifyIO(list -> deserialize(list, option.getElementType(), nbt));
		}

		public static <T> void deserialize(List<T> list, Class<T> type, NbtElement nbt) throws IOException {
			NbtList nbtList = NbtType.LIST.cast(nbt);
			INbtList inbtList = (INbtList)nbtList;
			NbtSerializer<T> serializer = NbtSerializers.get(type);

			list.clear();

			for (int i = 0; i < nbtList.size(); i++) {
				list.add(serializer.deserialize(inbtList.osl$config$get(i)));
			}
		}
	}

	public static class Maps {

		public static <K, V> NbtElement serialize(MapOption<K, V> option) throws IOException {
			return serialize(option.get(), option.getKeyType(), option.getValueType());
		}

		public static <K, V> NbtElement serialize(Map<K, V> map, Class<K> keyType, Class<V> valueType) throws IOException {
			NbtCompound mapNbt = new NbtCompound();

			List<K> keys = new ArrayList<>();
			List<V> values = new ArrayList<>();

			for (Map.Entry<K, V> entry : map.entrySet()) {
				keys.add(entry.getKey());
				values.add(entry.getValue());
			}

			mapNbt.put("keys", Lists.serialize(keys, keyType));
			mapNbt.put("values", Lists.serialize(values, valueType));

			return mapNbt;
		}

		public static <K, V> void deserialize(MapOption<K, V> option, NbtElement nbt) throws IOException {
			option.modifyIO(map -> deserialize(map, option.getKeyType(), option.getValueType(), nbt));
		}

		public static <K, V> void deserialize(Map<K, V> map, Class<K> keyType, Class<V> valueType, NbtElement nbt) throws IOException {
			NbtCompound mapNbt = NbtType.COMPOUND.cast(nbt);

			List<K> keys = new ArrayList<>();
			List<V> values = new ArrayList<>();

			Lists.deserialize(keys, keyType, ((INbtCompound)mapNbt).osl$config$get("keys"));
			Lists.deserialize(values, valueType, ((INbtCompound)mapNbt).osl$config$get("values"));

			if (keys.size() != values.size()) {
				throw new IOException("keys and values arrays should be the same length!");
			}

			map.clear();

			for (int i = 0; i < keys.size(); i++) {
				map.put(keys.get(i), values.get(i));
			}
		}
	}

	public static <O, S extends NbtSerializer<O>> S register(Class<O> type, S serializer) {
		return Registries.registerMapping(REGISTRY, type, serializer);
	}

	public static <O> NbtSerializer<O> get(Class<O> type) {
		return Registries.getMapping(REGISTRY, type);
	}
}
