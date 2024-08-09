package net.ornithemc.osl.config.api.serdes;

public interface MemorySerializerType<M> extends SerializerType<M> {

	M start();

}
