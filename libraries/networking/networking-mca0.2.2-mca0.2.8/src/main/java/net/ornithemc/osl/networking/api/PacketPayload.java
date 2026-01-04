package net.ornithemc.osl.networking.api;

import java.io.IOException;

public interface PacketPayload {

	void read(PacketBuffer buffer) throws IOException;

	void write(PacketBuffer buffer) throws IOException;

}
