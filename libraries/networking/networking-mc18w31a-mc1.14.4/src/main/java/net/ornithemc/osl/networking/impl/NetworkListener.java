package net.ornithemc.osl.networking.impl;

public class NetworkListener<T> {

	private final T listener;
	private final boolean async;

	public NetworkListener(T listener, boolean async) {
		this.listener = listener;
		this.async = async;
	}

	public T get() {
		return listener;
	}

	public boolean isAsync() {
		return async;
	}
}
