package net.ornithemc.osl.networking.impl;

public class NetworkListener<B, A> {

	private final B buffer;
	private final A array;

	public NetworkListener(B buffer, A array) {
		this.buffer = buffer;
		this.array = array;
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
}
