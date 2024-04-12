package net.ornithemc.osl.config.api.serdes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.Identifier;
import net.minecraft.util.math.BlockPos;

import net.ornithemc.osl.config.api.config.option.ListOption;
import net.ornithemc.osl.config.api.config.option.MapOption;
import net.ornithemc.osl.core.api.registry.Registries;
import net.ornithemc.osl.core.api.registry.Registry;

public class NetworkSerializers {

	private static final Registry<Class<?>, NetworkSerializer<?>> REGISTRY = Serializers.register(MinecraftSerializerTypes.NETWORK, "network");

	public static final NetworkSerializer<Boolean> BOOLEAN = register(Boolean.class, new NetworkSerializer<Boolean>() {

		@Override
		public void serialize(Boolean value, PacketByteBuf buffer) throws IOException {
			buffer.writeBoolean(value);
		}

		@Override
		public Boolean deserialize(PacketByteBuf buffer) throws IOException {
			return buffer.readBoolean();
		}
	});
	public static final NetworkSerializer<Byte> BYTE = register(Byte.class, new NetworkSerializer<Byte>() {

		@Override
		public void serialize(Byte value, PacketByteBuf buffer) throws IOException {
			buffer.writeByte(value);
		}

		@Override
		public Byte deserialize(PacketByteBuf buffer) throws IOException {
			return buffer.readByte();
		}
	});
	public static final NetworkSerializer<Double> DOUBLE = register(Double.class, new NetworkSerializer<Double>() {

		@Override
		public void serialize(Double value, PacketByteBuf buffer) throws IOException {
			buffer.writeDouble(value);
		}

		@Override
		public Double deserialize(PacketByteBuf buffer) throws IOException {
			return buffer.readDouble();
		}
	});
	public static final NetworkSerializer<Float> FLOAT = register(Float.class, new NetworkSerializer<Float>() {

		@Override
		public void serialize(Float value, PacketByteBuf buffer) throws IOException {
			buffer.writeFloat(value);
		}

		@Override
		public Float deserialize(PacketByteBuf buffer) throws IOException {
			return buffer.readFloat();
		}
	});
	public static final NetworkSerializer<Integer> INTEGER = register(Integer.class, new NetworkSerializer<Integer>() {

		@Override
		public void serialize(Integer value, PacketByteBuf buffer) throws IOException {
			buffer.writeInt(value);
		}

		@Override
		public Integer deserialize(PacketByteBuf buffer) throws IOException {
			return buffer.readInt();
		}
	});
	public static final NetworkSerializer<Long> LONG = register(Long.class, new NetworkSerializer<Long>() {

		@Override
		public void serialize(Long value, PacketByteBuf buffer) throws IOException {
			buffer.writeLong(value);
		}

		@Override
		public Long deserialize(PacketByteBuf buffer) throws IOException {
			return buffer.readLong();
		}
	});
	public static final NetworkSerializer<Short> SHORT = register(Short.class, new NetworkSerializer<Short>() {

		@Override
		public void serialize(Short value, PacketByteBuf buffer) throws IOException {
			buffer.writeShort(value);
		}

		@Override
		public Short deserialize(PacketByteBuf buffer) throws IOException {
			return buffer.readShort();
		}
	});
	public static final NetworkSerializer<Character> CHARACTER = register(Character.class, new NetworkSerializer<Character>() {

		@Override
		public void serialize(Character value, PacketByteBuf buffer) throws IOException {
			STRING.serialize(value.toString(), buffer);
		}

		@Override
		public Character deserialize(PacketByteBuf buffer) throws IOException {
			return STRING.deserialize(buffer).charAt(0);
		}
	});
	public static final NetworkSerializer<String> STRING = register(String.class, new NetworkSerializer<String>() {

		private static final int MAX_LENGTH = Short.MAX_VALUE;

		@Override
		public void serialize(String value, PacketByteBuf buffer) throws IOException {
			buffer.writeString(value);
		}

		@Override
		public String deserialize(PacketByteBuf buffer) throws IOException {
			return buffer.readString(MAX_LENGTH);
		}
	});
	public static final NetworkSerializer<Path> PATH = register(Path.class, new NetworkSerializer<Path>() {

		@Override
		public void serialize(Path value, PacketByteBuf buffer) throws IOException {
			STRING.serialize(value.toAbsolutePath().toString(), buffer);
		}

		@Override
		public Path deserialize(PacketByteBuf buffer) throws IOException {
			return Paths.get(STRING.deserialize(buffer));
		}
	});
	public static final NetworkSerializer<UUID> UUID = register(UUID.class, new NetworkSerializer<UUID>() {

		@Override
		public void serialize(UUID value, PacketByteBuf buffer) throws IOException {
			buffer.writeUuid(value);
		}

		@Override
		public UUID deserialize(PacketByteBuf buffer) throws IOException {
			return buffer.readUuid();
		}
	});
	public static final NetworkSerializer<Identifier> IDENTIFIER = register(Identifier.class, new NetworkSerializer<Identifier>() {

		@Override
		public void serialize(Identifier value, PacketByteBuf buffer) throws IOException {
			buffer.writeIdentifier(value);
		}

		@Override
		public Identifier deserialize(PacketByteBuf buffer) throws IOException {
			return buffer.readIdentifier();
		}
	});
	public static final NetworkSerializer<BlockPos> BLOCK_POS = register(BlockPos.class, new NetworkSerializer<BlockPos>() {

		@Override
		public void serialize(BlockPos value, PacketByteBuf buffer) throws IOException {
			LONG.serialize(value.toLong());
		}

		@Override
		public BlockPos deserialize(PacketByteBuf buffer) throws IOException {
			return BlockPos.fromLong(LONG.deserialize(buffer));
		}
	});

	public static class Lists {

		public static <T> void serialize(ListOption<T> option, PacketByteBuf buffer) throws IOException {
			serialize(option.get(), option.getElementType(), buffer);
		}

		public static <T> void serialize(List<T> list, Class<T> type, PacketByteBuf buffer) throws IOException {
			NetworkSerializer<T> serializer = NetworkSerializers.get(type);

			buffer.writeInt(list.size());
			for (int i = 0; i < list.size(); i++) {
				serializer.serialize(list.get(i), buffer);
			}
		}

		public static <T> void deserialize(ListOption<T> option, PacketByteBuf buffer) throws IOException {
			option.modifyIO(list -> deserialize(list, option.getElementType(), buffer));
		}

		public static <T> void deserialize(List<T> list, Class<T> type, PacketByteBuf buffer) throws IOException {
			NetworkSerializer<T> serializer = NetworkSerializers.get(type);

			list.clear();

			int length = buffer.readInt();
			for (int i = 0; i < length; i++) {
				list.add(serializer.deserialize(buffer));
			}
		}
	}

	public static class Maps {

		public static <K, V> void serialize(MapOption<K, V> option, PacketByteBuf buffer) throws IOException {
			serialize(option.get(), option.getKeyType(), option.getValueType(), buffer);
		}

		public static <K, V> void serialize(Map<K, V> map, Class<K> keyType, Class<V> valueType, PacketByteBuf buffer) throws IOException {
			List<K> keys = new ArrayList<>();
			List<V> values = new ArrayList<>();

			for (Map.Entry<K, V> entry : map.entrySet()) {
				keys.add(entry.getKey());
				values.add(entry.getValue());
			}

			Lists.serialize(keys, keyType, buffer);
			Lists.serialize(values, valueType, buffer);
		}

		public static <K, V> void deserialize(MapOption<K, V> option, PacketByteBuf buffer) throws IOException {
			option.modifyIO(map -> deserialize(map, option.getKeyType(), option.getValueType(), buffer));
		}

		public static <K, V> void deserialize(Map<K, V> map, Class<K> keyType, Class<V> valueType, PacketByteBuf buffer) throws IOException {
			List<K> keys = new ArrayList<>();
			List<V> values = new ArrayList<>();

			Lists.deserialize(keys, keyType, buffer);
			Lists.deserialize(values, valueType, buffer);

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
