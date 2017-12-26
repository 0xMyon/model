package com.github.myon.model;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

public interface Product extends Thing {


	public static Thing create(final Thing... factors) {
		switch(factors.length) {

		case 1: return factors[0];
		default: return new Product() {
			@Override
			public @NonNull Thing[] factors() {
				return factors;
			}
			@Override
			public String toString() {
				return "("+Stream.of(factors).map(Object::toString).reduce((a,b)->a+","+b).orElse("")+")";
			}
			@Override
			public <T> T accept(Visitor<T> visitor) {
				return visitor.handle(this);
			}
			
		};
		}
	}

	@NonNull Thing[] factors();

	@Override
	public default @NonNull Type typeof() {
		return ProductType.create(Stream.of(factors()).map(Thing::typeof).toArray(Type[]::new));
	}

	@Override
	public default Thing evaluate() {
		return create(Stream.of(factors()).map(Thing::evaluate).toArray(Thing[]::new));
	}

	@Override
	public default boolean isEvaluable() {
		return Stream.of(factors()).anyMatch(Thing::isEvaluable);
	}


}
