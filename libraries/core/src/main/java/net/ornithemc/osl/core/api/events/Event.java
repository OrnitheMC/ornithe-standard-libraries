package net.ornithemc.osl.core.api.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import net.ornithemc.osl.core.api.util.function.TriConsumer;
import net.ornithemc.osl.core.api.util.function.TriFunction;

public class Event<T> {

	private final List<T> listeners;
	private final Function<List<T>, T> invokerFactory;
	private T invoker;

	private Event(Function<List<T>, T> invokerFactory) {
		this.listeners = new ArrayList<>();
		this.invokerFactory = invokerFactory;
	}

	public static <T> Event<T> of(Function<List<T>, T> invokerFactory) {
		return new Event<>(invokerFactory);
	}

	public static Event<Runnable> runnable() {
		return of(listeners -> () -> listeners.forEach(Runnable::run));
	}

	public static <T> Event<Consumer<T>> consumer() {
		return of(listeners -> t -> listeners.forEach(listener -> listener.accept(t)));
	}

	public static <T, U> Event<BiConsumer<T, U>> biConsumer() {
		return of(listeners -> (t, u) -> listeners.forEach(listener -> listener.accept(t, u)));
	}

	public static <T, U, V> Event<TriConsumer<T, U, V>> triConsumer() {
		return Event.of(listeners -> (t, u, v) -> listeners.forEach(listener -> listener.accept(t, u, v)));
	}

	/**
	 * <p>Cancels after any listener returns a value.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findFirst()
	 */
	public static <T, R> Event<Function<T, R>> functionFirst() {
		return of(listeners -> (t) -> listeners.stream().map(listener -> listener.apply(t))
			.findFirst().orElse(null));
	}

	/**
	 * <p>Cancels after any listener returns non-null.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findFirst()
	 */
	public static <T, U, R> Event<BiFunction<T, U, R>> biFunctionFirst() {
		return of(listeners -> (t, u) -> listeners.stream().map(listener -> listener.apply(t, u))
			.findFirst().orElse(null));
	}

	/**
	 * <p>Cancels after any listener returns a value.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findFirst()
	 */
	public static <T, U, V, R> Event<TriFunction<T, U, V, R>> triFunctionFirst() {
		return Event.of(listeners -> (t, u, v) -> listeners.stream().map(listener -> listener.apply(t, u, v))
			.findFirst().orElse(null));
	}

	/**
	 * <p>Cancels after any listener returns a value.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findFirst()
	 */
	public static <T, R> Event<Function<T, R>> functionFirstNonNull() {
		return of(listeners -> (t) -> listeners.stream().map(listener -> listener.apply(t))
			.findFirst().orElse(null));
	}

	/**
	 * <p>Cancels after any listener returns a value.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findFirst()
	 */
	public static <T, U, R> Event<BiFunction<T, U, R>> biFunctionFirstNonNull() {
		return of(listeners -> (t, u) -> listeners.stream().map(listener -> listener.apply(t, u))
			.findFirst().orElse(null));
	}

	/**
	 * <p>Non-deterministic, performance oriented.</p>
	 * <p>Cancels after any listener returns non-null.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findFirst()
	 */
	public static <T, U, V, R> Event<TriFunction<T, U, V, R>> triFunctionFirstNonNull() {
		return Event.of(listeners -> (t, u, v) -> listeners.stream().map(listener -> listener.apply(t, u, v))
			.filter(Objects::nonNull).findFirst().orElse(null));
	}

	/**
	 * <p>Cancels after any listener returns non-null.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findFirst()
	 */
	public static <T, R> Event<Function<T, R>> functionAny() {
		return of(listeners -> (t) -> listeners.stream().map(listener -> listener.apply(t))
			.findAny().orElse(null));
	}

	/**
	 * <p>Cancels after any listener returns non-null.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findFirst()
	 */
	public static <T, U, R> Event<BiFunction<T, U, R>> biFunctionAny() {
		return of(listeners -> (t, u) -> listeners.stream().map(listener -> listener.apply(t, u))
			.findAny().orElse(null));
	}

	/**
	 * <p>Non-deterministic, performance oriented.</p>
	 * <p>Cancels after any listener returns a value.</p>
	 * <p>{@link #triFunctionFirst()} should be used instead of this in most cases.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findAny()
	 */
	public static <T, U, V, R> Event<TriFunction<T, U, V, R>> triFunctionAny() {
		return of(listeners -> (t, u, v) -> listeners.stream().map(listener -> listener.apply(t, u, v))
			.findAny().orElse(null));
	}

	/**
	 * <p>Non-deterministic, performance oriented.</p>
	 * <p>Cancels after any listener returns a value.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findAny()
	 */
	public static <T, R> Event<Function<T, R>> functionAnyNonNull() {
		return of(listeners -> (t) -> listeners.stream().map(listener -> listener.apply(t))
			.findAny().orElse(null));
	}

	/**
	 * <p>Non-deterministic, performance oriented.</p>
	 * <p>Cancels after any listener returns a value.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findAny()
	 */
	public static <T, U, R> Event<BiFunction<T, U, R>> biFunctionAnyNonNull() {
		return of(listeners -> (t, u) -> listeners.stream().map(listener -> listener.apply(t, u))
			.findAny().orElse(null));
	}

	/**
	 * <p>Non-deterministic, performance oriented.</p>
	 * <p>Cancels after any listener returns non-null.</p>
	 * <p>{@link #triFunctionFirstNonNull()} should be used instead of this in most cases.</p>
	 *
	 * @author halotroop2288
	 * @see Stream#findAny()
	 */
	public static <T, U, V, R> Event<TriFunction<T, U, V, R>> triFunctionAnyNonNull() {
		return of(listeners -> (t, u, v) -> listeners.stream().map(listener -> listener.apply(t, u, v))
			.filter(Objects::nonNull).findAny().orElse(null));
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
