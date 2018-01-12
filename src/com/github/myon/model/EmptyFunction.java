package com.github.myon.model;

public interface EmptyFunction extends CompositeFunction, UnionFunction, Abstraction {





	@Override
	default Type codomain(final Type parameter) {
		// TODO Auto-generated method stub
		return CompositeFunction.super.codomain(parameter);
	}

	@Override
	default boolean isEvaluable() {
		return false;
	}

	@Override
	default Type domain() {
		return Void.INSTANCE;
	}

	@Override
	default Epsilon isEqual(final Thing that) {
		// TODO Auto-generated method stub
		return CompositeFunction.super.isEqual(that);
	}

	@Override
	default Function evaluate() {
		return this;
	}

	@Override
	default Thing evaluate(final Thing parameter) {
		return Nothing.of("undefined");
	}

}
