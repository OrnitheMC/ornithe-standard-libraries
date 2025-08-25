package net.ornithemc.osl.networking.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.Identifier;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;

public class PacketBuffer extends ByteBuf {

	private static final int VAR_VALUE_BITS = 7;
	private static final int VAR_VALUE_MASK = 1 << VAR_VALUE_BITS - 1;
	private static final int VAR_PARITY_VALUE = 1 << VAR_VALUE_BITS;
	private static final int VAR_INT_MAX_BYTES = 5;
	private static final int VAR_LONG_MAX_BYTES = 10;

	private static final int TEXT_JSON_MAX_LENGTH = 0x40000;

	// this module spans 13w41a-18w30b but BlockPos to long packing was added in 14w03a
	private static final boolean SUPPORT_BLOCKPOS_PACKING = (BlockPos.class.getSuperclass() == Vec3i.class);

	final PacketByteBuf delegate;

	public PacketBuffer(ByteBuf delegate) {
		this(new PacketByteBuf(delegate));
	}

	public PacketBuffer(PacketByteBuf delegate) {
		this.delegate = delegate;
	}

	public int readVarInt() {
		return this.delegate.readVarInt();
	}

	public long readVarLong() {
		long value = 0;

		byte bytes = 0;
		byte nextByte = 0;

		do {
			nextByte = this.readByte();
			value |= (nextByte & VAR_VALUE_MASK) << bytes++ * VAR_VALUE_BITS;

			if (bytes > VAR_LONG_MAX_BYTES) {
				throw new RuntimeException("VarLong too big");
			}
		} while ((nextByte & VAR_PARITY_VALUE) == VAR_PARITY_VALUE);

		return value;
	}

	public byte[] readByteArray() {
		return this.readByteArray(this.readableBytes());
	}

	public byte[] readByteArray(int maxLength) {
		int length = this.readVarInt();

		if (length > maxLength) {
			throw new RuntimeException("ByteArray with size " + length + " is bigger than allowed " + maxLength);
		}

		byte[] values = new byte[length];

		for (int i = 0; i < length; i++) {
			values[i] = this.readByte();
		}

		return values;
	}

	public int[] readIntArray() {
		return this.readIntArray(this.readableBytes());
	}

	public int[] readIntArray(int maxLength) {
		int length = this.readVarInt();

		if (length > maxLength) {
			throw new RuntimeException("IntArray with size " + length + " is bigger than allowed " + maxLength);
		}

		int[] values = new int[length];

		for (int i = 0; i < length; i++) {
			values[i] = this.readVarInt();
		}

		return values;
	}

	public long[] readLongArray() {
		return this.readLongArray(this.readableBytes() / 8);
	}

	public long[] readLongArray(int maxLength) {
		int length = this.readVarInt();

		if (length > maxLength) {
            throw new IllegalStateException("LongArray with size " + length + " is bigger than allowed " + maxLength);
        }

        long[] values = new long[length];

        for (int i = 0; i < length; i++) {
            values[i] = this.readVarLong();
        }

        return values;
	}

	public String readString() {
		return this.readString(Short.MAX_VALUE);
	}

	public String readString(int maxLength) {
		int length = this.readVarInt();

		if (length > maxLength * 4) {
			throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")");
		}
		if (length < 0) {
			throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
		}

		String s = this.toString(this.readerIndex(), length, StandardCharsets.UTF_8);
		this.readerIndex(this.readerIndex() + length);

		if (s.length() > maxLength) {
			throw new RuntimeException("The received string length is longer than maximum allowed (" + length + " > " + maxLength + ")");
		}

		return s;
	}

	public <T extends Enum<T>> T readEnum(Class<T> type) {
		return type.getEnumConstants()[this.readVarInt()];
	}

	public Date readDate() {
		return new Date(this.readLong());
	}

	public UUID readUuid() {
		return new UUID(
			this.readLong(),
			this.readLong()
		);
	}

	public BlockPos readBlockPos() {
		if (SUPPORT_BLOCKPOS_PACKING) {
			return BlockPos.fromLong(this.readLong());
		} else {
			throw new UnsupportedOperationException("BlockPos packing is not supported in this Minecraft version!");
		}
	}

	public Identifier readIdentifier() {
		return new Identifier(this.readString());
	}

	public NamespacedIdentifier readNamespacedIdentifier() {
		return NamespacedIdentifiers.parse(this.readString());
	}

	public Text readText() {
		return Text.Serializer.fromJson(this.readString(TEXT_JSON_MAX_LENGTH));
	}

	public NbtCompound readNbtCompound() {
		return this.delegate.readNbtCompound();
	}

	public ItemStack readItem() {
		return this.delegate.readItem();
	}

	public ByteBuf writeVarInt(int value) {
		return this.delegate.writeVarInt(value);
	}

	public ByteBuf writeVarLong(long value) {
		while ((value & -128) != 0) {
			this.writeByte((int) (value & VAR_VALUE_MASK) | VAR_PARITY_VALUE);
			value >>>= VAR_VALUE_BITS;
		}

		this.writeByte((int) value);

		return this;
	}

	public ByteBuf writeByteArray(byte[] values) {
		this.writeVarInt(values.length);

		for (byte value : values) {
			this.writeByte(value);
		}

		return this;
	}

	public ByteBuf writeIntArray(int[] values) {
		this.writeVarInt(values.length);

		for (int value : values) {
			this.writeVarInt(value);
		}

		return this;
	}

	public ByteBuf writeLongArray(long[] values) {
		this.writeVarInt(values.length);

		for (long value : values) {
			this.writeVarLong(value);
		}

		return this;
	}

	public ByteBuf writeString(String s) {
		return this.writeString(s, Short.MAX_VALUE);
	}

	public ByteBuf writeString(String s, int maxLength) {
		byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

		if (bytes.length > maxLength) {
			throw new RuntimeException("String too big (was " + bytes.length + " bytes encoded, max " + maxLength + ")");
		}

		this.writeByteArray(bytes);

		return this;
	}

	public ByteBuf writeEnum(Enum<?> value) {
		this.writeVarInt(value.ordinal());

		return this;
	}

	public ByteBuf writeDate(Date date) {
		this.writeLong(date.getTime());

		return this;
	}

	public ByteBuf writeUuid(UUID uuid) {
		this.writeLong(uuid.getMostSignificantBits());
		this.writeLong(uuid.getLeastSignificantBits());

		return this;
	}

	public ByteBuf writeBlockPos(BlockPos pos) {
		if (SUPPORT_BLOCKPOS_PACKING) {
			this.writeLong(pos.toLong());
		} else {
			throw new UnsupportedOperationException("BlockPos packing is not supported in this Minecraft version!");
		}

		return this;
	}

	public ByteBuf writeIdentifier(Identifier id) {
		return this.writeString(id.toString());
	}

	public ByteBuf writeNamespacedIdentifier(NamespacedIdentifier id) {
		return this.writeString(id.toString());
	}

	public ByteBuf writeText(Text text) {
		return this.writeString(Text.Serializer.toJson(text), TEXT_JSON_MAX_LENGTH);
	}

	public ByteBuf writeNbtCompound(NbtCompound nbt) {
		return this.delegate.writeNbtCompound(nbt);
	}

	public ByteBuf writeItem(ItemStack item) {
		return this.delegate.writeItem(item);
	}

	@Override
	public int capacity() {
		return this.delegate.capacity();
	}

	@Override
	public ByteBuf capacity(int newCapacity) {
		return this.delegate.capacity(newCapacity);
	}

	@Override
	public int maxCapacity() {
		return this.delegate.maxCapacity();
	}

	@Override
	public ByteBufAllocator alloc() {
		return this.delegate.alloc();
	}

	@Override
	public ByteOrder order() {
		return this.delegate.order();
	}

	@Override
	public ByteBuf order(ByteOrder order) {
		return this.delegate.order(order);
	}

	@Override
	public ByteBuf unwrap() {
		return this.delegate.unwrap();
	}

	@Override
	public boolean isDirect() {
		return this.delegate.isDirect();
	}

	@Override
	public boolean isReadOnly() {
		return this.delegate.isReadOnly();
	}

	@Override
	public ByteBuf asReadOnly() {
		return this.delegate.asReadOnly();
	}

	@Override
	public int readerIndex() {
		return this.delegate.readerIndex();
	}

	@Override
	public ByteBuf readerIndex(int readerIndex) {
		return this.delegate.readerIndex(readerIndex);
	}

	@Override
	public int writerIndex() {
		return this.delegate.writerIndex();
	}

	@Override
	public ByteBuf writerIndex(int writerIndex) {
		return this.delegate.writerIndex(writerIndex);
	}

	@Override
	public ByteBuf setIndex(int readerIndex, int writerIndex) {
		return this.delegate.setIndex(readerIndex, writerIndex);
	}

	@Override
	public int readableBytes() {
		return this.delegate.readableBytes();
	}

	@Override
	public int writableBytes() {
		return this.delegate.writableBytes();
	}

	@Override
	public int maxWritableBytes() {
		return this.delegate.maxWritableBytes();
	}

	@Override
	public boolean isReadable() {
		return this.delegate.isReadable();
	}

	@Override
	public boolean isReadable(int size) {
		return this.delegate.isReadable(size);
	}

	@Override
	public boolean isWritable() {
		return this.delegate.isWritable();
	}

	@Override
	public boolean isWritable(int size) {
		return this.delegate.isWritable(size);
	}

	@Override
	public ByteBuf clear() {
		return this.delegate.clear();
	}

	@Override
	public ByteBuf markReaderIndex() {
		return this.delegate.markReaderIndex();
	}

	@Override
	public ByteBuf resetReaderIndex() {
		return this.delegate.resetReaderIndex();
	}

	@Override
	public ByteBuf markWriterIndex() {
		return this.delegate.markWriterIndex();
	}

	@Override
	public ByteBuf resetWriterIndex() {
		return this.delegate.resetWriterIndex();
	}

	@Override
	public ByteBuf discardReadBytes() {
		return this.delegate.discardReadBytes();
	}

	@Override
	public ByteBuf discardSomeReadBytes() {
		return this.delegate.discardSomeReadBytes();
	}

	@Override
	public ByteBuf ensureWritable(int minWritableBytes) {
		return this.delegate.ensureWritable(minWritableBytes);
	}

	@Override
	public int ensureWritable(int minWritableBytes, boolean force) {
		return this.delegate.ensureWritable(minWritableBytes, force);
	}

	@Override
	public boolean getBoolean(int index) {
		return this.delegate.getBoolean(index);
	}

	@Override
	public byte getByte(int index) {
		return this.delegate.getByte(index);
	}

	@Override
	public short getUnsignedByte(int index) {
		return this.delegate.getUnsignedByte(index);
	}

	@Override
	public short getShort(int index) {
		return this.delegate.getShort(index);
	}

	@Override
	public short getShortLE(int index) {
		return this.delegate.getShortLE(index);
	}

	@Override
	public int getUnsignedShort(int index) {
		return this.delegate.getUnsignedShort(index);
	}

	@Override
	public int getUnsignedShortLE(int index) {
		return this.delegate.getUnsignedShortLE(index);
	}

	@Override
	public int getMedium(int index) {
		return this.delegate.getMedium(index);
	}

	@Override
	public int getMediumLE(int index) {
		return this.delegate.getMediumLE(index);
	}

	@Override
	public int getUnsignedMedium(int index) {
		return this.delegate.getUnsignedMedium(index);
	}

	@Override
	public int getUnsignedMediumLE(int index) {
		return this.delegate.getUnsignedMediumLE(index);
	}

	@Override
	public int getInt(int index) {
		return this.delegate.getInt(index);
	}

	@Override
	public int getIntLE(int index) {
		return this.delegate.getIntLE(index);
	}

	@Override
	public long getUnsignedInt(int index) {
		return this.delegate.getUnsignedInt(index);
	}

	@Override
	public long getUnsignedIntLE(int index) {
		return this.delegate.getUnsignedIntLE(index);
	}

	@Override
	public long getLong(int index) {
		return this.delegate.getLong(index);
	}

	@Override
	public long getLongLE(int index) {
		return this.delegate.getLongLE(index);
	}

	@Override
	public char getChar(int index) {
		return this.delegate.getChar(index);
	}

	@Override
	public float getFloat(int index) {
		return this.delegate.getFloat(index);
	}

	@Override
	public double getDouble(int index) {
		return this.delegate.getDouble(index);
	}

	@Override
	public ByteBuf getBytes(int index, ByteBuf dst) {
		return this.delegate.getBytes(index, dst);
	}

	@Override
	public ByteBuf getBytes(int index, ByteBuf dst, int length) {
		return this.delegate.getBytes(index, dst, length);
	}

	@Override
	public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
		return this.delegate.getBytes(index, dst, dstIndex, length);
	}

	@Override
	public ByteBuf getBytes(int index, byte[] dst) {
		return this.delegate.getBytes(index, dst);
	}

	@Override
	public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
		return this.delegate.getBytes(index, dst, dstIndex, length);
	}

	@Override
	public ByteBuf getBytes(int index, ByteBuffer dst) {
		return this.delegate.getBytes(index, dst);
	}

	@Override
	public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
		return this.delegate.getBytes(index, out, length);
	}

	@Override
	public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
		return this.delegate.getBytes(index, out, length);
	}

	@Override
	public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
		return this.delegate.getBytes(index, out, position, length);
	}

	@Override
	public CharSequence getCharSequence(int index, int length, Charset charset) {
		return this.delegate.getCharSequence(index, length, charset);
	}

	@Override
	public ByteBuf setBoolean(int index, boolean value) {
		return this.delegate.setBoolean(index, value);
	}

	@Override
	public ByteBuf setByte(int index, int value) {
		return this.delegate.setByte(index, value);
	}

	@Override
	public ByteBuf setShort(int index, int value) {
		return this.delegate.setShort(index, value);
	}

	@Override
	public ByteBuf setShortLE(int index, int value) {
		return this.delegate.setShortLE(index, value);
	}

	@Override
	public ByteBuf setMedium(int index, int value) {
		return this.delegate.setMedium(index, value);
	}

	@Override
	public ByteBuf setMediumLE(int index, int value) {
		return this.delegate.setMediumLE(index, value);
	}

	@Override
	public ByteBuf setInt(int index, int value) {
		return this.delegate.setInt(index, value);
	}

	@Override
	public ByteBuf setIntLE(int index, int value) {
		return this.delegate.setIntLE(index, value);
	}

	@Override
	public ByteBuf setLong(int index, long value) {
		return this.delegate.setLong(index, value);
	}

	@Override
	public ByteBuf setLongLE(int index, long value) {
		return this.delegate.setLongLE(index, value);
	}

	@Override
	public ByteBuf setChar(int index, int value) {
		return this.delegate.setChar(index, value);
	}

	@Override
	public ByteBuf setFloat(int index, float value) {
		return this.delegate.setFloat(index, value);
	}

	@Override
	public ByteBuf setDouble(int index, double value) {
		return this.delegate.setDouble(index, value);
	}

	@Override
	public ByteBuf setBytes(int index, ByteBuf src) {
		return this.delegate.setBytes(index, src);
	}

	@Override
	public ByteBuf setBytes(int index, ByteBuf src, int length) {
		return this.delegate.setBytes(index, src, length);
	}

	@Override
	public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
		return this.delegate.setBytes(index, src, srcIndex, length);
	}

	@Override
	public ByteBuf setBytes(int index, byte[] src) {
		return this.delegate.setBytes(index, src);
	}

	@Override
	public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
		return this.delegate.setBytes(index, src, srcIndex, length);
	}

	@Override
	public ByteBuf setBytes(int index, ByteBuffer src) {
		return this.delegate.setBytes(index, src);
	}

	@Override
	public int setBytes(int index, InputStream in, int length) throws IOException {
		return this.delegate.setBytes(index, in, length);
	}

	@Override
	public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
		return this.delegate.setBytes(index, in, length);
	}

	@Override
	public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
		return this.delegate.setBytes(index, in, position, length);
	}

	@Override
	public ByteBuf setZero(int index, int length) {
		return this.delegate.setZero(index, length);
	}

	@Override
	public int setCharSequence(int index, CharSequence sequence, Charset charset) {
		return this.delegate.setCharSequence(index, sequence, charset);
	}

	@Override
	public boolean readBoolean() {
		return this.delegate.readBoolean();
	}

	@Override
	public byte readByte() {
		return this.delegate.readByte();
	}

	@Override
	public short readUnsignedByte() {
		return this.delegate.readUnsignedByte();
	}

	@Override
	public short readShort() {
		return this.delegate.readShort();
	}

	@Override
	public short readShortLE() {
		return this.delegate.readShortLE();
	}

	@Override
	public int readUnsignedShort() {
		return this.delegate.readUnsignedShort();
	}

	@Override
	public int readUnsignedShortLE() {
		return this.delegate.readUnsignedShortLE();
	}

	@Override
	public int readMedium() {
		return this.delegate.readMedium();
	}

	@Override
	public int readMediumLE() {
		return this.delegate.readMediumLE();
	}

	@Override
	public int readUnsignedMedium() {
		return this.delegate.readUnsignedMedium();
	}

	@Override
	public int readUnsignedMediumLE() {
		return this.delegate.readUnsignedMediumLE();
	}

	@Override
	public int readInt() {
		return this.delegate.readInt();
	}

	@Override
	public int readIntLE() {
		return this.delegate.readIntLE();
	}

	@Override
	public long readUnsignedInt() {
		return this.delegate.readUnsignedInt();
	}

	@Override
	public long readUnsignedIntLE() {
		return this.delegate.readUnsignedIntLE();
	}

	@Override
	public long readLong() {
		return this.delegate.readLong();
	}

	@Override
	public long readLongLE() {
		return this.delegate.readLongLE();
	}

	@Override
	public char readChar() {
		return this.delegate.readChar();
	}

	@Override
	public float readFloat() {
		return this.delegate.readFloat();
	}

	@Override
	public double readDouble() {
		return this.delegate.readDouble();
	}

	@Override
	public ByteBuf readBytes(int length) {
		return this.delegate.readBytes(length);
	}

	@Override
	public ByteBuf readSlice(int length) {
		return this.delegate.readSlice(length);
	}

	@Override
	public ByteBuf readRetainedSlice(int length) {
		return this.delegate.readRetainedSlice(length);
	}

	@Override
	public ByteBuf readBytes(ByteBuf dst) {
		return this.delegate.readBytes(dst);
	}

	@Override
	public ByteBuf readBytes(ByteBuf dst, int length) {
		return this.delegate.readBytes(dst, length);
	}

	@Override
	public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
		return this.delegate.readBytes(dst, dstIndex, length);
	}

	@Override
	public ByteBuf readBytes(byte[] dst) {
		return this.delegate.readBytes(dst);
	}

	@Override
	public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
		return this.delegate.readBytes(dst, dstIndex, length);
	}

	@Override
	public ByteBuf readBytes(ByteBuffer dst) {
		return this.delegate.readBytes(dst);
	}

	@Override
	public ByteBuf readBytes(OutputStream out, int length) throws IOException {
		return this.delegate.readBytes(out, length);
	}

	@Override
	public int readBytes(GatheringByteChannel out, int length) throws IOException {
		return this.delegate.readBytes(out, length);
	}

	@Override
	public CharSequence readCharSequence(int length, Charset charset) {
		return this.delegate.readCharSequence(length, charset);
	}

	@Override
	public int readBytes(FileChannel out, long position, int length) throws IOException {
		return this.delegate.readBytes(out, position, length);
	}

	@Override
	public ByteBuf skipBytes(int length) {
		return this.delegate.skipBytes(length);
	}

	@Override
	public ByteBuf writeBoolean(boolean value) {
		return this.delegate.writeBoolean(value);
	}

	@Override
	public ByteBuf writeByte(int value) {
		return this.delegate.writeByte(value);
	}

	@Override
	public ByteBuf writeShort(int value) {
		return this.delegate.writeShort(value);
	}

	@Override
	public ByteBuf writeShortLE(int value) {
		return this.delegate.writeShortLE(value);
	}

	@Override
	public ByteBuf writeMedium(int value) {
		return this.delegate.writeMedium(value);
	}

	@Override
	public ByteBuf writeMediumLE(int value) {
		return this.delegate.writeMediumLE(value);
	}

	@Override
	public ByteBuf writeInt(int value) {
		return this.delegate.writeInt(value);
	}

	@Override
	public ByteBuf writeIntLE(int value) {
		return this.delegate.writeIntLE(value);
	}

	@Override
	public ByteBuf writeLong(long value) {
		return this.delegate.writeLong(value);
	}

	@Override
	public ByteBuf writeLongLE(long value) {
		return this.delegate.writeLongLE(value);
	}

	@Override
	public ByteBuf writeChar(int value) {
		return this.delegate.writeChar(value);
	}

	@Override
	public ByteBuf writeFloat(float value) {
		return this.delegate.writeFloat(value);
	}

	@Override
	public ByteBuf writeDouble(double value) {
		return this.delegate.writeDouble(value);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf src) {
		return this.delegate.writeBytes(src);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf src, int length) {
		return this.delegate.writeBytes(src, length);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
		return this.delegate.writeBytes(src, srcIndex, length);
	}

	@Override
	public ByteBuf writeBytes(byte[] src) {
		return this.delegate.writeBytes(src);
	}

	@Override
	public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
		return this.delegate.writeBytes(src, srcIndex, length);
	}

	@Override
	public ByteBuf writeBytes(ByteBuffer src) {
		return this.delegate.writeBytes(src);
	}

	@Override
	public int writeBytes(InputStream in, int length) throws IOException {
		return this.delegate.writeBytes(in, length);
	}

	@Override
	public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
		return this.delegate.writeBytes(in, length);
	}

	@Override
	public int writeBytes(FileChannel in, long position, int length) throws IOException {
		return this.delegate.writeBytes(in, position, length);
	}

	@Override
	public ByteBuf writeZero(int length) {
		return this.delegate.writeZero(length);
	}

	@Override
	public int writeCharSequence(CharSequence sequence, Charset charset) {
		return this.delegate.writeCharSequence(sequence, charset);
	}

	@Override
	public int indexOf(int fromIndex, int toIndex, byte value) {
		return this.delegate.indexOf(fromIndex, toIndex, value);
	}

	@Override
	public int bytesBefore(byte value) {
		return this.delegate.bytesBefore(value);
	}

	@Override
	public int bytesBefore(int length, byte value) {
		return this.delegate.bytesBefore(length, value);
	}

	@Override
	public int bytesBefore(int index, int length, byte value) {
		return this.delegate.bytesBefore(index, length, value);
	}

	@Override
	public int forEachByte(ByteProcessor processor) {
		return this.delegate.forEachByte(processor);
	}

	@Override
	public int forEachByte(int index, int length, ByteProcessor processor) {
		return this.delegate.forEachByte(index, length, processor);
	}

	@Override
	public int forEachByteDesc(ByteProcessor processor) {
		return this.delegate.forEachByteDesc(processor);
	}

	@Override
	public int forEachByteDesc(int index, int length, ByteProcessor processor) {
		return this.delegate.forEachByteDesc(index, length, processor);
	}

	@Override
	public ByteBuf copy() {
		return this.delegate.copy();
	}

	@Override
	public ByteBuf copy(int index, int length) {
		return this.delegate.copy(index, length);
	}

	@Override
	public ByteBuf slice() {
		return this.delegate.slice();
	}

	@Override
	public ByteBuf retainedSlice() {
		return this.delegate.retainedSlice();
	}

	@Override
	public ByteBuf slice(int index, int length) {
		return this.delegate.slice(index, length);
	}

	@Override
	public ByteBuf retainedSlice(int index, int length) {
		return this.delegate.retainedSlice(index, length);
	}

	@Override
	public ByteBuf duplicate() {
		return this.delegate.duplicate();
	}

	@Override
	public ByteBuf retainedDuplicate() {
		return this.delegate.retainedDuplicate();
	}

	@Override
	public int nioBufferCount() {
		return this.delegate.nioBufferCount();
	}

	@Override
	public ByteBuffer nioBuffer() {
		return this.delegate.nioBuffer();
	}

	@Override
	public ByteBuffer nioBuffer(int index, int length) {
		return this.delegate.nioBuffer(index, length);
	}

	@Override
	public ByteBuffer internalNioBuffer(int index, int length) {
		return this.delegate.internalNioBuffer(index, length);
	}

	@Override
	public ByteBuffer[] nioBuffers() {
		return this.delegate.nioBuffers();
	}

	@Override
	public ByteBuffer[] nioBuffers(int index, int length) {
		return this.delegate.nioBuffers(index, length);
	}

	@Override
	public boolean hasArray() {
		return this.delegate.hasArray();
	}

	@Override
	public byte[] array() {
		return this.delegate.array();
	}

	@Override
	public int arrayOffset() {
		return this.delegate.arrayOffset();
	}

	@Override
	public boolean hasMemoryAddress() {
		return this.delegate.hasMemoryAddress();
	}

	@Override
	public long memoryAddress() {
		return this.delegate.memoryAddress();
	}

	@Override
	public String toString(Charset charset) {
		return this.delegate.toString(charset);
	}

	@Override
	public String toString(int index, int length, Charset charset) {
		return this.delegate.toString(index, length, charset);
	}

	@Override
	public int hashCode() {
		return this.delegate.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return this.delegate.equals(o);
	}

	@Override
	public int compareTo(ByteBuf o) {
		return this.delegate.compareTo(o);
	}

	@Override
	public String toString() {
		return this.delegate.toString();
	}

	@Override
	public ByteBuf retain(int increment) {
		return this.delegate.retain(increment);
	}

	@Override
	public ByteBuf retain() {
		return this.delegate.retain();
	}

	@Override
	public ByteBuf touch() {
		return this.delegate.touch();
	}

	@Override
	public ByteBuf touch(Object hint) {
		return this.delegate.touch(hint);
	}

	@Override
	public int refCnt() {
		return this.delegate.refCnt();
	}

	@Override
	public boolean release() {
		return this.delegate.release();
	}

	@Override
	public boolean release(int decrement) {
		return this.delegate.release(decrement);
	}
}
