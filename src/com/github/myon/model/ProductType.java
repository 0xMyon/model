package com.github.myon.model;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

public interface ProductType extends Type {


	Type[] factors();

	static ProductType create(final Type... factors) {
		return new ProductType() {
			@Override
			public Type[] factors() {
				return factors;
			}
		};
	}


	@Override
	public default boolean isEvaluable() {
		return Stream.of(factors()).allMatch(Type::isEvaluable);
	}

	@Override
	public @NonNull	default Type evaluate() {
		return create(Stream.of(factors()).map(Type::evaluate).toArray(Type[]::new));
	}

	@Override
	public default boolean intersetcs(final Type type) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public default boolean containsAll(final Type type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public default boolean contains(@NonNull final Thing thing) {
		// TODO Auto-generated method stub
		return false;
	}



}
