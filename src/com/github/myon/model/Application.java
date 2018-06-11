package com.github.myon.model;

/**
 * Application of a {@link Function} to a {@link Thing} parameter
 * @author 0xMyon
 *
 */
public interface Application<DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>> extends Thing<CODOMAIN> {

	Function<?,? super DOMAIN, ? extends CODOMAIN> function();

	Thing<? extends DOMAIN> parameter();

	static <DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>>
	Thing<? extends CODOMAIN>
	of(final Function<?, ? super DOMAIN, ? extends CODOMAIN> function, final Thing<? extends DOMAIN> thing) {
		return new Application<DOMAIN, CODOMAIN>() {

			@Override
			public Thing<? extends DOMAIN> parameter() {
				return thing;
			}

			@Override
			public Function<?,? super DOMAIN, ? extends CODOMAIN> function() {
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

			@Override
			public CODOMAIN THIS() {
				//TODO add impl
				return evaluate().THIS();
			}


		};


	}


	@Override
	default Epsilon<?> isEqual(final Thing<?> that) {
		return null;
	}


	@Override
	default Type<?> typeof() {
		return function().typeof().codomain(parameter().typeof());
	}

	@Override
	default boolean isEvaluable() {
		return true;
	}

	@Override
	public default Thing<? extends CODOMAIN> evaluate() {
		if (function().isEvaluable()) {
			return Application.<DOMAIN,CODOMAIN>of(function().evaluate(), parameter());
		} else if (parameter().isEvaluable()) {
			return Application.<DOMAIN,CODOMAIN>of(function(), parameter());
		} else {
			return function().evaluate(parameter());
		}
	}

}
