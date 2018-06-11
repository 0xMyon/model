package com.github.myon.model.function;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.type.FunctionType;


public interface Composition<THIS extends Composition<THIS,DOMAIN,TYPE,CODOMAIN>, DOMAIN extends Thing<DOMAIN>, TYPE extends Thing<TYPE>, CODOMAIN extends Thing<CODOMAIN>> extends Function<THIS, DOMAIN, CODOMAIN> {

	//Stream<? extends Function> elements();

	Function<?,? super DOMAIN, ? extends TYPE> first();
	Function<?,? super TYPE, ? extends CODOMAIN> second();


	@Override
	default Epsilon isEqual(final Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.of("unequal");
			}
			@Override
			public Epsilon handle(final Composition<? super Thing,?,? extends Thing> that) {
				return Epsilon.Conjunction();
			}
		});
	}

	static <DOMAIN extends Thing, TYPE extends Thing, CODOMAIN extends Thing>
	Function<? super DOMAIN, ? extends CODOMAIN>
	of(final Function<? super DOMAIN, ? extends TYPE> first, final Function<? super TYPE, ? extends CODOMAIN> second) {
		return new Composition<DOMAIN,TYPE,CODOMAIN>() {

			@Override
			public Function<? super DOMAIN, ? extends TYPE> first() {
				return first;
			}
			@Override
			public Function<? super TYPE, ? extends CODOMAIN> second() {
				return second;
			}

			@Override
			public <T> T accept(final Visitor<T,DOMAIN,CODOMAIN> visitor) {
				return visitor.handle(this);
			}
			@Override
			public String toString() {
				return first.toString()+"."+second.toString();
			}
		};

	}


	@Override
	public default boolean isEvaluable() {
		return first().isEvaluable() || second().isEvaluable();
	}

	@Override
	default FunctionType typeof() {
		return FunctionType.of(
				first().typeof().domain(), t -> second().typeof().codomain(first().typeof().codomain(t))
				);
	}


	@Override
	public default Composition<THIS,DOMAIN,TYPE,CODOMAIN> evaluate() {
		return Composition.of(first().evaluate(), second().evaluate());
	}


	@Override
	public default CODOMAIN evaluate(final Thing<? extends DOMAIN> parameter) {
		return second().evaluate(first().evaluate(parameter));
	}


}
