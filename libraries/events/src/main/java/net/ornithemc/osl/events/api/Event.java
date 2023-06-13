package net.ornithemc.osl.events.api;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Event<L> {

	public static <L> Event<L> of(Consumer<List<L>> invoker) {
		return new Event<>(invoker);
	}

	public static <L> Event<L> simple(Consumer<L> simpleInvoker) {
		return new Event<>(listeners -> {
			for (int i = 0; i < listeners.size(); i++) {
				simpleInvoker.accept(listeners.get(i));
			}
		});
	}

	private final List<L> listeners;
	private final Consumer<List<L>> invoker;

	private Event(Consumer<List<L>> invoker) {
		this.listeners = new ArrayList<>();
		this.invoker = invoker;
	}

	public void register(L listener) {
		listeners.add(listener);
	}

	public void run() {
		invoker.accept(listeners);
	}
}
