package net.ornithemc.osl.config.impl.config.serdes;

import java.nio.file.Path;

import net.ornithemc.osl.config.api.config.serdes.BaseConfigSerializer;

public class JsonConfigSerializer extends BaseConfigSerializer<Path> {

	private Path path;

	@Override
	public void initWrite(Path path) {
		this.path = path;
	}

	@Override
	public void initRead(Path path) {
		this.path = path;
	}

	@Override
	protected void writeName(String name) {
		
	}

	@Override
	protected void writeVersion(int version) {
		
	}

	@Override
	protected String readName() {
		return null;
	}

	@Override
	protected int readVersion() {
		return 0;
	}
}
