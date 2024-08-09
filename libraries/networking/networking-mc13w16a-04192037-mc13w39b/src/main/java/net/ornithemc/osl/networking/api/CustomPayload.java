package net.ornithemc.osl.networking.api;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface CustomPayload {

	void read(DataInput input) throws IOException;

	void write(DataOutput output) throws IOException;

}
