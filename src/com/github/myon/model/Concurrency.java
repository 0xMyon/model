package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.type.ConcurrencyType;

public interface Concurrency extends Thing {

	Stream<? extends Thing> threads();

	static Thing of(final Stream<Thing> threads) {
		return of(threads.toArray(Thing[]::new));
	}

	static Thing of(final Thing... threads) {
		switch (threads.length) {
		case 0:
			return Epsilon.INSTANCE;
		case 1:
			return threads[0];
		default:
			return new Concurrency() {
				@Override
				public <T> T accept(final Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public Stream<Thing> threads() {
					return Stream.of(threads);
				}
				@Override
				public int compareTo(final Thing that) {
					return that.accept(new Thing.Visitor<Integer>() {
						@Override
						public Integer handle(final Thing that) {
							return getClass().getName().compareTo(that.getClass().getName());
						}
						@Override
						public Integer handle(final Concurrency that) {
							try {
								return Streams.zip(threads(), that.threads(), Thing::compareTo).reduce(0, (a,b)->a+b);
							} catch (final Nothing e) {
								return (int) (threads().count() - that.threads().count());
							}
						}
					});
				}
			};
		}
	}


	@Override
	default Type typeof() {
		return ConcurrencyType.of(threads().map(Thing::typeof));
	}

	@Override
	default Epsilon isEqual(final Thing that) {
		return that.accept(new Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.of("");
			}
			@Override
			public Epsilon handle(final Concurrency that) {
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
	default Thing evaluate() {
		return of(threads().map(Thing::evaluate));
	}


}
