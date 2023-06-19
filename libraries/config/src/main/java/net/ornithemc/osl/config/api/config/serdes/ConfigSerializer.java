package net.ornithemc.osl.config.api.config.serdes;

import net.ornithemc.osl.config.api.config.Config;

public interface ConfigSerializer<M> {

	void serialize(M medium, Config config);

	void deserialize(M medium, Config config);

}
