package net.ornithemc.osl.config.api.serdes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.config.api.config.option.ListOption;
import net.ornithemc.osl.config.api.config.option.MapOption;
import net.ornithemc.osl.core.api.io.DataStream;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class NetworkSerializers {

	private static final Registry<Class<?>, NetworkSerializer<?>> REGISTRY = Serializers.register(MinecraftSerializerTypes.NETWORK, "network");

	public static final NetworkSerializer<Boolean> BOOLEAN = register(Boolean.class, new NetworkSerializer<Boolean>() {

		@Override
		public void serialize(Boolean value, DataStream data) throws IOException {
			data.writeBoolean(value);
		}

		@Override
		public Boolean deserialize(DataStream data) throws IOException {
			return data.readBoolean();
		}
	});
	public static final NetworkSerializer<Byte> BYTE = register(Byte.class, new NetworkSerializer<Byte>() {

		@Override
		public void serialize(Byte value, DataStream data) throws IOException {
			data.writeByte(value);
		}

		@Override
		public Byte deserialize(DataStream data) throws IOException {
			return data.readByte();
		}
	});
	public static final NetworkSerializer<Double> DOUBLE = register(Double.class, new NetworkSerializer<Double>() {

		@Override
		public void serialize(Double value, DataStream data) throws IOException {
			data.writeDouble(value);
		}

		@Override
		public Double deserialize(DataStream data) throws IOException {
			return data.readDouble();
		}
	});
	public static final NetworkSerializer<Float> FLOAT = register(Float.class, new NetworkSerializer<Float>() {

		@Override
		public void serialize(Float value, DataStream data) throws IOException {
			data.writeFloat(value);
		}

		@Override
		public Float deserialize(DataStream data) throws IOException {
			return data.readFloat();
		}
	});
	public static final NetworkSerializer<Integer> INTEGER = register(Integer.class, new NetworkSerializer<Integer>() {

		@Override
		public void serialize(Integer value, DataStream data) throws IOException {
			data.writeInt(value);
		}

		@Override
		public Integer deserialize(DataStream data) throws IOException {
			return data.readInt();
		}
	});
	public static final NetworkSerializer<Long> LONG = register(Long.class, new NetworkSerializer<Long>() {

		@Override
		public void serialize(Long value, DataStream data) throws IOException {
			data.writeLong(value);
		}

		@Override
		public Long deserialize(DataStream data) throws IOException {
			return data.readLong();
		}
	});
	public static final NetworkSerializer<Short> SHORT = register(Short.class, new NetworkSerializer<Short>() {

		@Override
		public void serialize(Short value, DataStream data) throws IOException {
			data.writeShort(value);
		}

		@Override
		public Short deserialize(DataStream data) throws IOException {
			return data.readShort();
		}
	});
	public static final NetworkSerializer<Character> CHARACTER = register(Character.class, new NetworkSerializer<Character>() {

		@Override
		public void serialize(Character value, DataStream data) throws IOException {
			STRING.serialize(value.toString(), data);
		}

		@Override
		public Character deserialize(DataStream data) throws IOException {
			return STRING.deserialize(data).charAt(0);
		}
	});
	public static final NetworkSerializer<String> STRING = register(String.class, new NetworkSerializer<String>() {

		@Override
		public void serialize(String value, DataStream data) throws IOException {
			data.writeUTF(value);
		}

		@Override
		public String deserialize(DataStream data) throws IOException {
			return data.readUTF();
		}
	});
	public static final NetworkSerializer<Path> PATH = register(Path.class, new NetworkSerializer<Path>() {

		@Override
		public void serialize(Path value, DataStream data) throws IOException {
			STRING.serialize(value.toAbsolutePath().toString(), data);
		}

		@Override
		public Path deserialize(DataStream data) throws IOException {
			return Paths.get(STRING.deserialize(data));
		}
	});
	public static final NetworkSerializer<UUID> UUID = register(UUID.class, new NetworkSerializer<UUID>() {

		@Override
		public void serialize(UUID value, DataStream data) throws IOException {
			LONG.serialize(value.getMostSignificantBits());
			LONG.serialize(value.getLeastSignificantBits());
		}

		@Override
		public UUID deserialize(DataStream data) throws IOException {
			return new UUID(
				LONG.deserialize(data),
				LONG.deserialize(data)
			);
		}
	});
	public static final NetworkSerializer<BlockPos> BLOCK_POS = register(BlockPos.class, new NetworkSerializer<BlockPos>() {

		@Override
		public void serialize(BlockPos value, DataStream data) throws IOException {
			INTEGER.serialize(value.x);
			INTEGER.serialize(value.y);
			INTEGER.serialize(value.z);
		}

		@Override
		public BlockPos deserialize(DataStream data) throws IOException {
			return new BlockPos(
				INTEGER.deserialize(data),
				INTEGER.deserialize(data),
				INTEGER.deserialize(data)
			);
		}
	});

	public static class Lists {

		public static <T> void serialize(ListOption<T> option, DataStream data) throws IOException {
			serialize(option.get(), option.getElementType(), data);
		}

		public static <T> void serialize(List<T> list, Class<T> type, DataStream data) throws IOException {
			NetworkSerializer<T> serializer = NetworkSerializers.get(type);

			data.writeInt(list.size());
			for (int i = 0; i < list.size(); i++) {
				serializer.serialize(list.get(i), data);
			}
		}

		public static <T> void deserialize(ListOption<T> option, DataStream data) throws IOException {
			option.modifyIO(list -> deserialize(list, option.getElementType(), data));
		}

		public static <T> void deserialize(List<T> list, Class<T> type, DataStream data) throws IOException {
			NetworkSerializer<T> serializer = NetworkSerializers.get(type);

			list.clear();

			int length = data.readInt();
			for (int i = 0; i < length; i++) {
				list.add(serializer.deserialize(data));
			}
		}
	}

	public static class Maps {

		public static <K, V> void serialize(MapOption<K, V> option, DataStream data) throws IOException {
			serialize(option.get(), option.getKeyType(), option.getValueType(), data);
		}

		public static <K, V> void serialize(Map<K, V> map, Class<K> keyType, Class<V> valueType, DataStream data) throws IOException {
			List<K> keys = new ArrayList<>();
			List<V> values = new ArrayList<>();

			for (Map.Entry<K, V> entry : map.entrySet()) {
				keys.add(entry.getKey());
				values.add(entry.getValue());
			}

			Lists.serialize(keys, keyType, data);
			Lists.serialize(values, valueType, data);
		}

		public static <K, V> void deserialize(MapOption<K, V> option, DataStream data) throws IOException {
			option.modifyIO(map -> deserialize(map, option.getKeyType(), option.getValueType(), data));
		}

		public static <K, V> void deserialize(Map<K, V> map, Class<K> keyType, Class<V> valueType, DataStream data) throws IOException {
			List<K> keys = new ArrayList<>();
			List<V> values = new ArrayList<>();

			Lists.deserialize(keys, keyType, data);
			Lists.deserialize(values, valueType, data);

			if (keys.size() != values.size()) {
				throw new IOException("keys and values arrays should be the same length!");
			}

			map.clear();

			for (int i = 0; i < keys.size(); i++) {
				map.put(keys.get(i), values.get(i));
			}
		}
	}

	public static <O, S extends NetworkSerializer<O>> S register(Class<O> type, S serializer) {
		return Registries.registerMapping(REGISTRY, type, serializer);
	}

	public static <O> NetworkSerializer<O> get(Class<O> type) {
		return Registries.getMapping(REGISTRY, type);
	}
}
