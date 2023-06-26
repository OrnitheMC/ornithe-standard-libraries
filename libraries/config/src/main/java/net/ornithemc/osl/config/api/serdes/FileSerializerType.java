package net.ornithemc.osl.config.api.serdes;

import java.nio.file.Path;

public interface FileSerializerType<M> extends SerializerType<M> {

	M open(Path path);

}
