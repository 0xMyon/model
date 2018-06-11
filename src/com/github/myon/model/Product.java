package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.type.ProductType;

public interface Product<THIS extends Product<THIS, E>, E extends Thing<E>> extends Thing<THIS> {


	static <E extends Thing<E>> Product<?,E> of(final Stream<Thing<? extends E>> factors) {
		return of(factors.toArray(Thing[]::new));
	}


	@SafeVarargs
	static <E extends Thing<E>> Product<?,E> of(final Thing<? extends E>... factors) {
		switch(factors.length) {
		case 0:
			return Epsilon.INSTANCE;
		case 1:
			return factors[0];
		default:
			return new Impl<E>() {
				@Override
				public  Stream<Thing<? extends E>> factors() {
					return Stream.of(factors);
				}
				@Override
				public String toString() {
					return "("+Stream.of(factors).map(Object::toString).reduce((a,b)->a+","+b).orElse("")+")";
				}
			};
		}
	}

	interface Impl<E extends Thing<E>> extends Product<Impl<E>, E> {
		@Override
		public default Impl<E> THIS() {
			return this;
		}
		@Override
		public default <T> T accept(final Visitor<T> visitor) {
			return visitor.handle(this);
		}
	}

	@Override
	default Epsilon<?> isEqual( final Thing<?> that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.of("unequal");
			}
			@Override
			public Epsilon handle(final Product that) {
				return Epsilon.Conjunction();
			}
		});
	}

	Stream<Thing<? extends E>> factors();

	@Override
	public default Type<?> typeof() {
		return ProductType.of(factors().map(Thing::typeof));
	}

	@Override
	public default Product<? extends THIS, E> evaluate() {
		return of(factors().map(Thing::evaluate));
	}

	@Override
	public default boolean isEvaluable() {
		return factors().anyMatch(Thing::isEvaluable);
	}


}
