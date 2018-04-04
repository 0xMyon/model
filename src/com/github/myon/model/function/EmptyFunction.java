package com.github.myon.model.function;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.Void;

public interface EmptyFunction extends Composition, UnionFunction, Abstraction {





	@Override
	default Type codomain(final Type parameter) {
		// TODO Auto-generated method stub
		return Composition.super.codomain(parameter);
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
		return Composition.super.isEqual(that);
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
