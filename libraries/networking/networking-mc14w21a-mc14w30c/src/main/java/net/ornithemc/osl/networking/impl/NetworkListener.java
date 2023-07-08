package net.ornithemc.osl.networking.impl;

public class NetworkListener<B, A> {

	private final B buffer;
	private final A array;
	private final boolean async;

	public NetworkListener(B buffer, A array, boolean async) {
		this.buffer = buffer;
		this.array = array;
		this.async = async;
	}

	public B buffer() {
		return buffer;
	}

	public A array() {
		return array;
	}

	public boolean isBuffer() {
		return buffer != null;
	}

	public boolean isAsync() {
		return async;
	}
}
