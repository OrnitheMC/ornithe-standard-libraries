package net.ornithemc.osl.networking.impl;

public class NetworkListener<B, A> {

	private final B stream;
	private final A array;

	public NetworkListener(B stream, A array) {
		this.stream = stream;
		this.array = array;
	}

	public B stream() {
		return stream;
	}

	public A array() {
		return array;
	}

	public boolean isStream() {
		return stream != null;
	}
}
