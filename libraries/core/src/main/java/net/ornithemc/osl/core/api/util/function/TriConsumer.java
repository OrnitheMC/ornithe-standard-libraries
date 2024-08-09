package net.ornithemc.osl.core.api.util.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * <p>
 * Represents an operation that accepts three inputs argument and returns no result.<br>
 * This is the two-arity specialization of {@link Consumer}.
 * Unlike most other functional interfaces, {@code TriConsumer} is expected
 * to operate via side-effects.
 * </p>
 * <p>
 * This is a {@link FunctionalInterface} whose functional method is {@link #accept(Object, Object, Object)}.
 * </p>
 *
 * @param <T> the type of the first input to the operation
 * @param <U> the type of the second input to the operation
 * @param <V> the type of the third input to the operation
 * @author halotroop2288
 * @see java.util.function.BiConsumer
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {
	/**
	 * Performs this operation on the given arguments.
	 *
	 * @param t the first input argument
	 * @param u the second input argument
	 * @param v the second input argument
	 * @author halotroop2288
	 */
	void accept(T t, U u, V v);

	/**
	 * Returns a composed {@code TriConsumer} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation, throws an exception, it is relayed to the caller of the
	 * composed operation.  If performing this operation throws an exception,
	 * the {@code after} operation will not be performed.
	 *
	 * @param after the operation to perform after this operation
	 * @return a composed {@code BiConsumer} that performs in sequence this
	 * operation followed by the {@code after} operation
	 * @throws NullPointerException if {@code after} is null
	 * @author halotroop2288
	 */
	default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
		Objects.requireNonNull(after);

		return (l, m, n) -> {
			accept(l, m, n);
			after.accept(l, m, n);
		};
	}
}
