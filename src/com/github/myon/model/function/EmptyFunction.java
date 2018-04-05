package com.github.myon.model.function;

import java.util.stream.Stream;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;

public interface EmptyFunction extends Composition, UnionFunction, Abstraction {

	@Override
	default boolean isEvaluable() {
		return false;
	}

	@Override
	default Epsilon isEqual(final Thing that) {
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

	static EmptyFunction INSTANCE = new EmptyFunction() {
		@Override
		public <T> T accept(final Visitor<T> visitor) {
			return visitor.handle(this);
		}

	};

	@Override
	default Function implementation() {
		return Nothing.of("undefined");
	}

	@Override
	default Stream<? extends Nothing> superposed() {
		return Stream.of();
	}


	@Override
	default Stream<? extends Nothing> elements() {
		return Stream.of();
	}

}
