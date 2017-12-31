package com.github.myon.model;

import java.util.stream.Stream;

public interface ProductType extends Type {


	Stream<? extends Type> factors();

	static Type of(final Stream<Type> factors) {
		return of(factors.toArray(Type[]::new));
	}

	static Type of(final Type... factors) {
		switch (factors.length) {
		case 1:
			return factors[0];
		default:
			return new ProductType() {
				@Override
				public Stream<Type> factors() {
					return Stream.of(factors);
				}
				@Override
				public String toString() {
					return factors().map(Object::toString).reduce((a,b)->a+" ** "+b).orElse("[()]");
				}
				@Override
				public <T> T accept(final Visitor<T> visitor) {
					return visitor.handle(this);
				}
			};
		}
	}


	@Override
	public default boolean isEvaluable() {
		return factors().anyMatch(Type::isEvaluable);
	}

	@Override
	public 	default Type evaluate() {
		return of(factors().map(Type::evaluate));
	}

	@Override
	public default Epsilon intersetcs(final Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {

			@Override
			public Epsilon handle(final Type that) {
				return Nothing.of("doeas not intersect");
			}

			@Override
			public Epsilon handle(final ProductType that) {
				try {
					return Epsilon.Conjunction(Streams.zip(factors(), that.factors(), Type::intersetcs));
				} catch (final Nothing e) {
					return e;
				}
			}
		});
	}



	@Override
	public default Epsilon containsAll(final Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Type that) {
				return Nothing.of(that.toString()+" is no subtype");
			}
			@Override
			public Epsilon handle(final ProductType that) {
				try {
					return Epsilon.Conjunction(Streams.zip(factors(), that.factors(), Type::containsAll));
				} catch (final Nothing e) {
					return e;
				}
			}

		});
	}

	@Override
	public default Epsilon contains( final Thing thing) {
		return thing.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.TypeMiss(ProductType.this, thing);
			}
			@Override
			public Epsilon handle(final Product that) {
				try {
					return Epsilon.Conjunction(Streams.zip(factors(), that.factors(), Type::contains));
				} catch (final Nothing e) {
					return e;
				}
			}
			@Override
			public Epsilon handle(final Nothing that) {
				return Epsilon.INSTANCE;
			}
		});
	}

}
