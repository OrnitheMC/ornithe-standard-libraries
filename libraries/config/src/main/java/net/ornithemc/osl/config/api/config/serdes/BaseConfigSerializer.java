package net.ornithemc.osl.config.api.config.serdes;

import net.ornithemc.osl.config.api.config.Config;
import net.ornithemc.osl.config.api.config.option.group.OptionGroup;

public abstract class BaseConfigSerializer<M> implements ConfigSerializer<M> {

	@Override
	public void serialize(M medium, Config config) {
		initWrite(medium);

		writeName(config.getName());
		writeVersion(config.getVersion());

		for (OptionGroup group : config.getGroups()) {
			
		}
	}

	protected abstract void initWrite(M medium);

	protected abstract void writeName(String name);

	protected abstract void writeVersion(int version);

	@Override
	public void deserialize(M medium, Config config) {
		initRead(medium);

		String name = readName();
		int version = readVersion();
	}

	protected abstract void initRead(M medium);

	protected abstract String readName();

	protected abstract int readVersion();

}
