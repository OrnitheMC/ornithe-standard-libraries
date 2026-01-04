package net.ornithemc.osl.entrypoints.api;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public interface RunArgsConsumer {

	void defineOptions(OptionParser parser);

	void parseOptions(OptionSet options);

}
