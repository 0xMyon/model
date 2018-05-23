package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.type.ConcurrencyType;

import util.Streams;

public interface Concurrency<THIS extends Concurrency<THIS, E>, E extends Thing<E>> extends Thing<THIS> {

	Stream<? extends Thing<? extends THIS>> threads();

	static <THIS extends Concurrency<THIS, E>, E extends Thing<E>>
	Thing<? extends THIS> of(final Stream<? extends E> threads) {
		return of(threads.toArray(Thing[]::new));
	}

	@SafeVarargs
	static <THIS extends Concurrency<THIS, E>, E extends Thing<E>>
	Thing<? extends THIS> of(final Thing<? extends E>... threads) {
		switch (threads.length) {
		case 0:
			return Epsilon.INSTANCE;
		case 1:
			return threads[0];
		default:
			return new Impl<E>() {

				@Override
				public Stream<? extends E> threads() {
					return Stream.of(threads);
				}

			};
		}
	}

	static abstract class Impl<E extends Thing<E>> implements Concurrency<Impl<E>, E> {

		@Override
		public Impl<E> THIS() {
			return this;
		}

		@Override
		public <T> T accept(final Thing.Visitor<T> visitor) {
			return visitor.handle(this);
		}

	}

	@Override
	default Type typeof() {
		return ConcurrencyType.of(threads().map(Thing::typeof));
	}

	@Override
	default Epsilon<?> isEqual(final Thing<?> that) {
		return that.accept(new Visitor<Epsilon<?>>() {
			@Override
			public Epsilon<?> handle(final Thing<?> that) {
				return Nothing.of("");
			}
			@Override
			public Epsilon<?> handle(final Concurrency<?,?> that) {
				try {
					return Epsilon.Conjunction(Streams.zip(threads(), that.threads(), Thing::isEqual));
				} catch (final Nothing e) {
					return e;
				}
			}

		});
	}

	@Override
	default boolean isEvaluable() {
		return threads().anyMatch(Thing::isEvaluable);
	}

	@Override
	default Thing<? extends THIS> evaluate() {
		return of(threads().map(Thing::evaluate));
	}


}
