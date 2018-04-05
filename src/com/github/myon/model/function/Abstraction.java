package com.github.myon.model.function;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.type.FunctionType;

public interface Abstraction extends Function {

	Function implementation();

	static Abstraction of(final Type domain, final Function implementation) {
		return implementation.typeof().domain().containsAll(domain).accept(new Epsilon.Visitor<Abstraction>() {
			@Override
			public Abstraction handle(final Nothing that) {
				return that;
			}
			@Override
			public Abstraction handle(final Epsilon that) {
				return new Abstraction() {
					@Override
					public Function implementation() {
						return implementation;
					}
					@Override
					public <T> T accept(final Function.Visitor<T> visitor) {
						return visitor.handle(this);
					}
					@Override
					public FunctionType typeof() {
						return FunctionType.of(domain.evaluate(), t -> implementation.typeof().codomain(t));
					}
				};
			}

		});
	}



	@Override
	default Epsilon isEqual(final Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.of("unequal");
			}
			@Override
			public Epsilon handle(final Abstraction that) {
				return Epsilon.Conjunction(
						implementation().isEqual(that.implementation())
						);
			}
		});
	}

	@Override
	default boolean isEvaluable() {
		return implementation().isEvaluable();
	}

	@Override
	default Function evaluate() {
		return of(typeof().domain(), implementation().evaluate());
	}

	@Override
	default Thing evaluate(final Thing parameter) {
		return implementation().evaluate(parameter);
	}

}
