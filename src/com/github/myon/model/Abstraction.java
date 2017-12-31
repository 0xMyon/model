package com.github.myon.model;


public interface Abstraction extends Function {

	@Override
	Type domain();

	Function implementation();

	static Abstraction of(final Type domain, final Function implementation) {
		// TODO maybe evaluate types a compile time
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
					public Type domain() {
						return domain;
					}
					@Override
					public <T> T accept(final Function.Visitor<T> visitor) {
						return visitor.handle(this);
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
						domain().isEqual(that.domain()),
						implementation().isEqual(that.implementation())
						);
			}
		});
	}

	@Override
	default boolean isEvaluable() {
		return domain().isEvaluable() || implementation().isEvaluable();
	}



	@Override
	default Function evaluate() {
		return of(domain().evaluate(), implementation().evaluate());
	}

	@Override
	default Type codomain(final Type parameter) {
		return implementation().codomain(parameter);
	}

	@Override
	default Thing apply(final Thing parameter) {
		return implementation().apply(parameter);
	}

}
