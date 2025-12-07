package net.ornithemc.osl.networking.impl;

@SuppressWarnings("serial")
public final class NotOnMainThreadException extends RuntimeException {

	public static final NotOnMainThreadException INSTANCE = new NotOnMainThreadException();

	private NotOnMainThreadException() {
		super();
	}
}
