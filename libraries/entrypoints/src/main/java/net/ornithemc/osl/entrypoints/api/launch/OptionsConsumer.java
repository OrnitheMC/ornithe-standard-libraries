package net.ornithemc.osl.entrypoints.api.launch;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public interface OptionsConsumer {

	void defineOptions(OptionParser parser);

	void acceptOptions(OptionSet options);

}
