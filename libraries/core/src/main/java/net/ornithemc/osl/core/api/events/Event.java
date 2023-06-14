package net.ornithemc.osl.core.api.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Event<T> {

	public static <T> Event<T> of(Function<List<T>, T> invokerFactory) {
		return new Event<>(invokerFactory);
	}

	public static Event<Runnable> runnable() {
		return of(listeners -> {
			return () -> {
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).run();
				}
			};
		});
	}

	public static <T> Event<Consumer<T>> consumer() {
		return of(listeners -> {
			return value -> {
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).accept(value);
				}
			};
		});
	}

	private final List<T> listeners;
	private final Function<List<T>, T> invokerFactory;

	private T invoker;

	private Event(Function<List<T>, T> invokerFactory) {
		this.listeners = new ArrayList<>();
		this.invokerFactory = invokerFactory;
	}

	public void register(T listener) {
		listeners.add(listener);

		if (invoker != null) {
			invoker = null;
		}
	}

	public T invoker() {
		if (invoker == null) {
			invoker = invokerFactory.apply(listeners);
		}

		return invoker;
	}
}
