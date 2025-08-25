package net.ornithemc.osl.networking.api;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import net.ornithemc.osl.core.api.util.function.IOConsumer;
import net.ornithemc.osl.networking.impl.Constants;

public final class PacketBuffers {

	public static ByteBuffer make() {
		return ByteBuffer.allocate(Short.MAX_VALUE);
	}

	public static ByteBuffer make(byte[] bytes) {
		return ByteBuffer.wrap(bytes);
	}

	public static ByteBuffer make(ByteBuffer buffer) {
		return ByteBuffer.wrap(buffer.array(), buffer.arrayOffset(), buffer.limit());
	}

	public static ByteBuffer make(IOConsumer<ByteBuffer> writer) throws IOException {
		ByteBuffer buffer = make();
		writer.accept(buffer);
		return buffer;
	}

	public static byte[] bytes(ByteBuffer buffer) {
		byte[] bytes = new byte[buffer.position()];
		buffer.position(0);
		buffer.get(bytes);
		return bytes;
	}

	public static String readString(ByteBuffer buffer) throws IOException {
		return readString(buffer, Constants.DEFAULT_MAX_STRING_LENGTH);
	}

	public static String readString(ByteBuffer buffer, int maxLength) throws IOException {
		int length = buffer.getInt();

		if (length > 4 * maxLength) {
			throw new IOException("Received string buffer larger than maximum allowed (" + length + " > " + 4 * maxLength + ")");
		}
		if (length < 0) {
			throw new IOException("Received string buffer smaller than zero! Weird string!");
		}

		byte[] bytes = new byte[length];
		buffer.get(bytes);

		String s = new String(bytes, StandardCharsets.UTF_8);

		if (s.length() > maxLength) {
			throw new IOException("Received string longer than maximum allowed (" + s.length() + " > " + maxLength + ")");
		}

		return s;
	}

	public static void writeString(ByteBuffer buffer, String s) throws IOException {
		writeString(buffer, s, Constants.DEFAULT_MAX_STRING_LENGTH);
	}

	public static void writeString(ByteBuffer buffer, String s, int maxLength) throws IOException {
		if (s.length() > maxLength) {
			throw new IOException("Sent string longer than maximum allowed (" + s.length() + " > " + maxLength + ")");
		}

		byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

		buffer.putInt(bytes.length);
		buffer.put(bytes);
	}
}
