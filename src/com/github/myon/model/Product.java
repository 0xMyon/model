package com.github.myon.model;

import java.util.Arrays;
import java.util.stream.Stream;

import com.github.myon.model.type.ProductType;

public interface Product<THIS extends Product<THIS, E>, E extends Thing<E>> extends Thing<THIS> {


	Stream<Thing<? extends E>> factors();



	static <E extends Thing<E>> Thing<?> of(final Stream<Thing<? extends E>> factors) {
		final Thing<? extends E>[] array = factors.toArray(Thing[]::new);
		switch (array.length) {
		case 0:
			return of();
		case 1:
			return array[0];
		default:
			return Product.<E>of(array[0], array[1], Arrays.copyOfRange(array, 2, array.length));
		}

	}


	static Epsilon<?> of() {
		return Epsilon.INSTANCE;
	}

	static <E extends Thing<E>> E of(final E factor) {
		return factor;
	}

	@SafeVarargs
	static <E extends Thing<E>> Impl<E> of(final Thing<? extends E> first, final Thing<? extends E> second, final Thing<? extends E>... factors) {
		return new Impl<E>() {
			@Override
			public  Stream<Thing<? extends E>> rest() {
				return Stream.of(factors);
			}

			@Override
			public String toString() {
				return "("+Stream.of(factors()).map(Object::toString).reduce((a,b)->a+","+b).orElse("")+")";
			}

			@Override
			public Thing<? extends E> first() {
				return first;
			}
			@Override
			public Thing<? extends E> second() {
				return second;
			}
		};

	}

	interface Impl<E extends Thing<E>> extends Product<Impl<E>, E> {
		Thing<? extends E> first();
		Thing<? extends E> second();
		Stream<Thing<? extends E>> rest();


		@Override
		public default Stream<Thing<? extends E>> factors() {
			return Stream.concat(Stream.of(first(), second()), rest());
		}

		@Override
		public default Impl<E> THIS() {
			return this;
		}
		@Override
		public default <T> T accept(final Visitor<T> visitor) {
			return visitor.handle(this);
		}
		@Override
		public default Impl<E> evaluate() {
			return Product.<E>of(first().evaluate(), second().evaluate(), factors().<Thing<? extends E>>map(Thing::evaluate).toArray(Thing[]::new));
		}
	}

	@Override
	default Epsilon<?> isEqual( final Thing<?> that) {
		return that.accept(new Thing.Visitor<Epsilon<?>>() {
			@Override
			public Epsilon<?> handle(final Thing<?> that) {
				return Nothing.of("unequal");
			}
			@Override
			public Epsilon<?> handle(final Product<?,?> that) {
				return Epsilon.Conjunction();
			}
		});
	}


	@Override
	public default Type<?> typeof() {
		return ProductType.of(factors().map(Thing::typeof));
	}



	@Override
	public default boolean isEvaluable() {
		return factors().anyMatch(Thing::isEvaluable);
	}


}
