package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.type.ProductType;

public interface Product extends Thing {


	static Thing of(final Stream<Thing> factors) {
		return of(factors.toArray(Thing[]::new));
	}


	static Thing of(final Thing... factors) {
		switch(factors.length) {
		case 0:
			return Epsilon.INSTANCE;
		case 1:
			return factors[0];
		default:
			return new Product() {
				@Override
				public  Stream<Thing> factors() {
					return Stream.of(factors);
				}
				@Override
				public String toString() {
					return "("+Stream.of(factors).map(Object::toString).reduce((a,b)->a+","+b).orElse("")+")";
				}
				@Override
				public <T> T accept(final Visitor<T> visitor) {
					return visitor.handle(this);
				}
			};
		}
	}

	@Override

	default Epsilon isEqual( final Thing that) {
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

	Stream<? extends Thing> factors();

	@Override
	public default Type typeof() {
		return ProductType.of(factors().map(Thing::typeof));
	}

	@Override
	public default Thing evaluate() {
		return of(factors().map(Thing::evaluate));
	}

	@Override
	public default boolean isEvaluable() {
		return factors().anyMatch(Thing::isEvaluable);
	}


}
