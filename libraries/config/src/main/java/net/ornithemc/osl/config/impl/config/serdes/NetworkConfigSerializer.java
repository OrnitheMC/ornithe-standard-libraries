package net.ornithemc.osl.config.impl.config.serdes;

import java.nio.ByteBuffer;

import net.ornithemc.osl.config.api.config.serdes.BaseConfigSerializer;

public class NetworkConfigSerializer extends BaseConfigSerializer<ByteBuffer> {

	@Override
	protected void initWrite(ByteBuffer medium) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeVersion(int version) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initRead(ByteBuffer medium) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String readName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int readVersion() {
		// TODO Auto-generated method stub
		return 0;
	}
}
