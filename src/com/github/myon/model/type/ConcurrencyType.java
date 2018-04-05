package com.github.myon.model.type;

import java.util.stream.Stream;

import com.github.myon.model.Concurrency;
import com.github.myon.model.Epsilon;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;

import util.Streams;

public interface ConcurrencyType extends Type {

	Stream<? extends Type> threads();

	static Type of(final Stream<Type> threads) {
		return of(threads.toArray(Type[]::new));
	}

	static Type of(final Type... threads) {
		switch (threads.length) {
		case 0:
			return Epsilon.INSTANCE.typeof();
		case 1:
			return threads[0];
		default:
			return new ConcurrencyType() {
				@Override
				public <T> T accept(final Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public Stream<? extends Type> threads() {
					return Stream.of(threads);
				}
			};
		}
	}

	@Override
	default Concurrency cast(final Thing thing) {
		return thing.accept(new Thing.Visitor<Concurrency>() {
			@Override
			public Concurrency handle(final Concurrency that) {
				try {
					return (Concurrency) Concurrency.of(Streams.zip(threads(), that.threads(), Type::cast));
				} catch (final Nothing error) {
					return Nothing.of("Cast exception");
				}
			}
			@Override
			public Concurrency handle(final Thing that) {
				return Nothing.of("Cast exception");
			}
		});
	}

	@Override
	default boolean isEvaluable() {
		return threads().anyMatch(Type::isEvaluable);
	}

	@Override
	default Type evaluate() {
		return of(threads().map(Type::evaluate));
	}

	@Override
	default Epsilon intersetcs(final Type type) {
		return type.accept(new Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Type that) {
				return Nothing.of("incompatible");
			}
			@Override
			public Epsilon handle(final ConcurrencyType that) {
				try {
					return Epsilon.Conjunction(Streams.zip(threads(), that.threads(), Type::intersetcs));
				} catch (final Nothing e) {
					return e;
				}
			}
		});
	}

	@Override
	default Epsilon containsAll(final Type type) {
		return type.accept(new Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Type that) {
				return Nothing.of("incompatible");
			}
			@Override
			public Epsilon handle(final ConcurrencyType that) {
				try {
					return Epsilon.Conjunction(Streams.zip(threads(), that.threads(), Type::containsAll));
				} catch (final Nothing e) {
					return e;
				}
			}
		});
	}

	@Override
	default Epsilon contains(final Thing thing) {
		return thing.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.of("no object of");
			}
			@Override
			public Epsilon handle(final Concurrency that) {
				try {
					return Epsilon.Conjunction(Streams.zip(threads(), that.threads(), Type::contains));
				} catch (final Nothing e) {
					return e;
				}
			}
		});
	}

}
