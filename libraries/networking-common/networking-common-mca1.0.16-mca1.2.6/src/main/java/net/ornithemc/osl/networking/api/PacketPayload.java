package net.ornithemc.osl.networking.api;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface PacketPayload {

	void read(ByteBuffer buffer) throws IOException;

	void write(ByteBuffer buffer) throws IOException;

}
