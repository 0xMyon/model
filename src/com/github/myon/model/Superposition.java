package com.github.myon.model;

import java.util.stream.Stream;

import util.Streams;

public interface Superposition<THIS extends Superposition<THIS,E>, E extends Thing<E>> extends Thing<THIS> {

	Stream<? extends Thing<? extends E>> superposed();

	static abstract class Impl<E extends Thing<E>> implements Superposition<Impl<E>, E> {

		@Override
		public Impl<E> THIS() {
			return this;
		}

		@Override
		public <T> T accept(final Thing.Visitor<T> visitor) {
			return visitor.handle(this);
		}

		@Override
		public String toString() {
			return "{"+superposed().map(Object::toString).reduce((a,b) -> a+","+b).orElse("")+"}";
		}

	}

	static <THING extends Thing<THING>> Thing<? extends THING> of(final Stream<? extends Thing<? extends THING>> superposed) {
		return of(superposed.map(t -> t.accept(new Thing.Visitor<Stream<? extends Thing<? extends THING>>>() {
			@Override
			public Stream<Thing<? extends THING>> handle(final Thing<?> that) {
				return Stream.of(that);
			}
			@Override
			public Stream<? extends Thing<? extends THING>> handle(final Nothing that) {
				return Stream.of(that);
			}
			@Override
			public Stream<? extends Thing<? extends THING>> handle(final Superposition that) {
				return that.superposed();
			}
		})).reduce(Stream.of(), Stream::concat).toArray(Thing[]::new));
	}

	static <E extends Thing<E>> Thing<?> of(final Thing<? extends E>... summants) {
		switch (summants.length) {
		case 0:
			return Nothing.of("empty");
		case 1:
			return summants[0];
		default:
			return new Impl<E>() {

				@Override
				public Stream<? extends Thing<? extends E>> superposed() {
					return Stream.of(summants).distinct().sorted();
				}

			};
		}
	}

	@Override
	default Type<?> typeof() {
		return Type.Union(superposed().map(Thing::typeof));
	}

	@Override
	default boolean isEvaluable() {
		return superposed().anyMatch(Thing::isEvaluable);
	}

	@Override
	default Thing evaluate() {
		return of(superposed().map(Thing::evaluate));
	}

	@Override
	default Thing apply(final Function function) {
		return of(superposed().map(x -> x.apply(function)));
	}

	@Override
	default Epsilon isEqual( final Thing that) {
		return that.accept(new Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.of("not equal");
			}
			@Override
			public Epsilon handle(final Superposition that) {
				try {
					return Epsilon.Conjunction(Streams.zip(superposed(), that.superposed(), Thing::isEqual));
				} catch (final Nothing e) {
					return e;
				}
			}
		});
	}

}
