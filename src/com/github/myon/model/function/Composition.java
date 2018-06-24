package com.github.myon.model.function;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.type.FunctionType;


public interface Composition<THIS extends Composition<THIS,DOMAIN,TYPE,CODOMAIN>, DOMAIN extends Thing<DOMAIN>, TYPE extends Thing<TYPE>, CODOMAIN extends Thing<CODOMAIN>> extends Function<THIS, DOMAIN, CODOMAIN> {

	Function<?,? super DOMAIN, ? extends TYPE> first();
	Function<?,? super TYPE, ? extends CODOMAIN> second();

	@Override
	default Epsilon<?> isEqual(final Thing<?> that) {
		return that.accept(new Thing.Visitor<Epsilon<?>>() {
			@Override
			public Epsilon<?> handle(final Thing<?> that) {
				return Nothing.of("unequal");
			}
			@Override
			public Epsilon<?> handle(final Composition<?,?,?,?> that) {
				return Epsilon.Conjunction();
			}
		});
	}

	interface Impl<DOMAIN extends Thing<DOMAIN>, TYPE extends Thing<TYPE>, CODOMAIN extends Thing<CODOMAIN>> extends Composition<Impl<DOMAIN,TYPE,CODOMAIN>, DOMAIN, TYPE, CODOMAIN> {
		@Override
		default public <T> T accept(final Visitor<T> visitor) {
			return visitor.handle(this);
		}
		@Override
		default public Impl<DOMAIN, TYPE, CODOMAIN> THIS() {
			return this;
		}
		@Override
		public default Impl<DOMAIN, TYPE, CODOMAIN> evaluate() {
			return Composition.<DOMAIN,TYPE,CODOMAIN>of(
					first().evaluate(),
					second().evaluate()
					);
		}
	}

	static <DOMAIN extends Thing<DOMAIN>, TYPE extends Thing<TYPE>, CODOMAIN extends Thing<CODOMAIN>>
	Impl<DOMAIN,TYPE,CODOMAIN>
	of(final Function<?,? super DOMAIN, ? extends TYPE> first, final Function<?,? super TYPE, ? extends CODOMAIN> second) {
		return new Impl<DOMAIN,TYPE,CODOMAIN>() {

			@Override
			public Function<?,? super DOMAIN, ? extends TYPE> first() {
				return first;
			}
			@Override
			public Function<?,? super TYPE, ? extends CODOMAIN> second() {
				return second;
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
	default FunctionType<?> typeof() {
		return FunctionType.of(
				first().typeof().domain(), t -> second().typeof().codomain(first().typeof().codomain(t))
				);
	}


	@Override
	public Composition<? extends THIS, ? super DOMAIN, TYPE, ? extends CODOMAIN> evaluate();


	@Override
	public default Thing<? extends CODOMAIN> evaluate(final Thing<? extends DOMAIN> parameter) {
		return first().evaluate(parameter).apply(second());
	}


}
