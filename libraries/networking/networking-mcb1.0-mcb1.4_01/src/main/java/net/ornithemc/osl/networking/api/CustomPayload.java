package net.ornithemc.osl.networking.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface CustomPayload {

	void read(DataInputStream input) throws IOException;

	void write(DataOutputStream output) throws IOException;

}
