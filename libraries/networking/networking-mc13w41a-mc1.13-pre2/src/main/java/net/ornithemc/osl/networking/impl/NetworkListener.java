package net.ornithemc.osl.networking.impl;

public class NetworkListener<T> {

	private final T delegate;
	private final boolean async;

	public NetworkListener(T delegate, boolean async) {
		this.delegate = delegate;
		this.async = async;
	}

	public T get() {
		return delegate;
	}

	public boolean isAsync() {
		return async;
	}
}
