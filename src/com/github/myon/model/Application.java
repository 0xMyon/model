package com.github.myon.model;

/**
 * Application of a {@link Function} to a {@link Thing} parameter
 * @author 0xMyon
 *
 */
public interface Application<DOMAIN extends Thing, CODOMAIN extends Thing> extends Thing {

	Function<? super DOMAIN, ? extends CODOMAIN> function();
	DOMAIN parameter();

	static <DOMAIN extends Thing, CODOMAIN extends Thing>
	Application<? super DOMAIN, ? extends CODOMAIN> of(final Function<? super DOMAIN, ? extends CODOMAIN> function, final DOMAIN parameter) {
		return new Application<DOMAIN, CODOMAIN>() {
			@Override
			public DOMAIN parameter() {
				return parameter;
			}

			@Override
			public Function<? super DOMAIN, ? extends CODOMAIN> function() {
				return function;
			}
			@Override
			public String toString() {
				return function().toString()+"("+parameter().toString()+")";
			}

			@Override
			public <T> T accept(final Visitor<T> visitor) {
				return visitor.handle(this);
			}
		};


	}


	@Override
	default Epsilon isEqual(final Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.of("unequal");
			}
			@Override
			public Epsilon handle(final Application that) {
				return Epsilon.Conjunction(
						function().isEqual(that.function()),
						parameter().isEqual(that.parameter())
						);
			}
		});
	}


	@Override
	default Type typeof() {
		return function().typeof().codomain(parameter().typeof());
	}

	@Override
	default boolean isEvaluable() {
		return true;
	}

	@Override
	public default Thing evaluate() {
		if (function().isEvaluable()) {
			return of(function().evaluate(), parameter());
		} else if (parameter().isEvaluable()) {
			return of(function(), parameter().evaluate());
		} else {
			return parameter().accept(new Thing.Visitor<Thing>() {
				@Override
				public Thing handle(final Thing that) {
					return function().evaluate(parameter());
				}
				@Override
				public Thing handle(final Nothing that) {
					return function().evaluate(parameter());
				}
				@Override
				public Thing handle(final Superposition that) {
					return Thing.Superposition(that.superposed().map(function()::evaluate));
				}
			});
		}
	}

}
