package com.github.myon.model.function;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.type.FunctionType;

public interface Abstraction<THIS extends Abstraction<THIS, DOMAIN, CODOMAIN>, DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>> extends Function<THIS, DOMAIN, CODOMAIN> {

	Function<?,? super DOMAIN, ? extends CODOMAIN> implementation();

	static <DOMAIN extends Thing, CODOMAIN extends Thing>
	Abstraction<? super DOMAIN, ? extends CODOMAIN> of(
			final Type domain,
			final Function<?,? super DOMAIN, ? extends CODOMAIN> implementation) {
		return implementation.typeof().domain().containsAll(domain).accept(new Epsilon.Visitor<Abstraction<? super DOMAIN, ? extends CODOMAIN>>() {
			@Override
			public Abstraction<? super DOMAIN, ? extends CODOMAIN> handle(final Nothing that) {
				return (Abstraction<? super DOMAIN, ? extends CODOMAIN>) that;
			}
			@Override
			public Abstraction<DOMAIN, CODOMAIN> handle(final Epsilon that) {
				return new Abstraction<DOMAIN, CODOMAIN>() {
					@Override
					public Function<? super DOMAIN, ? extends CODOMAIN> implementation() {
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
			public Epsilon handle(final Abstraction<? extends Thing, ? extends Thing> that) {
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
	default Abstraction<THIS, DOMAIN, CODOMAIN> evaluate() {
		return of(typeof().domain(), implementation().evaluate());
	}

	@Override
	default CODOMAIN evaluate(final Thing<? extends DOMAIN> parameter) {
		return implementation().evaluate(parameter);
	}

}
