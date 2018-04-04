package com.github.myon.model;

import java.util.stream.Stream;

public interface Superposition extends Thing {

	Stream<? extends Thing> superposed();

	static Thing of(final Stream<? extends Thing> superposed) {
		return of(superposed.map(t -> t.accept(new Thing.Visitor<Stream<? extends Thing>>() {
			@Override
			public Stream<Thing> handle(final Thing that) {
				return Stream.of(that);
			}
			@Override
			public Stream<Thing> handle(final Nothing that) {
				return Stream.of(that);
			}
			@Override
			public Stream<? extends Thing> handle(final Superposition that) {
				return that.superposed();
			}
		})).reduce(Stream.of(), Stream::concat).toArray(Thing[]::new));
	}

	static Thing of(final Thing... summants) {
		switch (summants.length) {
		case 0:
			return Nothing.of("empty");
		case 1:
			return summants[0];
		default:
			return new Superposition() {
				@Override
				public <T> T accept(final Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public Stream<Thing> superposed() {
					return Stream.of(summants).distinct().sorted();
				}
				@Override
				public int compareTo(final Thing that) {
					return that.accept(new Thing.Visitor<Integer>() {
						@Override
						public Integer handle(final Thing that) {
							return getClass().getName().compareTo(that.getClass().getName());
						}
						@Override
						public Integer handle(final Superposition that) {
							try {
								return Streams.zip(superposed(), that.superposed(), Thing::compareTo).reduce(0, (a,b)->a+b);
							} catch (final Nothing e) {
								return (int) (superposed().count() - that.superposed().count());
							}
						}
					});
				}
				@Override
				public String toString() {
					return "{"+superposed().map(Object::toString).reduce((a,b) -> a+","+b).orElse("")+"}";
				}
			};
		}
	}

	@Override
	default  Type typeof() {
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
