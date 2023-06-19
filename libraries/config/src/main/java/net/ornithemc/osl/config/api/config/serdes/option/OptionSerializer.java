package net.ornithemc.osl.config.api.config.serdes.option;

import net.ornithemc.osl.config.api.config.option.Option;
import net.ornithemc.osl.config.api.config.serdes.ConfigSerializer;

public interface OptionSerializer<S extends ConfigSerializer<?>, O extends Option<?>> {

	void serialize(S serializer, O option);

	void deserialize(S serializer, O option);

}
