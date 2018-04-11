package com.github.myon.model.function;

import java.util.stream.Stream;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;

public interface EmptyFunction<DOMAIN extends Thing> extends Composition<DOMAIN,Nothing,Nothing>, UnionFunction<DOMAIN,Nothing>, Abstraction<DOMAIN,Nothing> {

	@Override
	default boolean isEvaluable() {
		return false;
	}

	@Override
	default Epsilon isEqual(final Thing that) {
		return Composition.super.isEqual(that);
	}

	@Override
	default EmptyFunction<? super DOMAIN> evaluate() {
		return this;
	}

	@Override
	default Nothing evaluate(final DOMAIN parameter) {
		return Nothing.of("undefined");
	}

	static <DOMAIN extends Thing, CODOMAIN extends Thing> EmptyFunction<? super DOMAIN> of() {
		return new EmptyFunction<DOMAIN>() {
			@Override
			public <T> T accept(final Visitor<T> visitor) {
				return visitor.handle(this);
			}
		};
	}

	@Override
	default EmptyFunction<? super DOMAIN> implementation() {
		return this;
	}

	@Override
	default Stream<? extends EmptyFunction<? super DOMAIN>> superposed() {
		return Stream.of();
	}


	@Override
	default Nothing first() {
		return Nothing.of("");
	}

	@Override
	default Nothing second() {
		return Nothing.of("");
	}

}
