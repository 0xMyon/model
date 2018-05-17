package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.function.Abstraction;
import com.github.myon.model.function.Composition;
import com.github.myon.model.function.EmptyFunction;
import com.github.myon.model.function.Identity;
import com.github.myon.model.function.SystemFunction;
import com.github.myon.model.function.UnionFunction;
import com.github.myon.model.type.FunctionType;

public interface Function<DOMAIN extends Thing, CODOMAIN extends Thing> extends Thing {

	static final Function<? super Thing,? extends Thing> ID = Identity.of(Type.ANYTHING);

	/**
	 * Applies parameters to the function
	 * @param parameter
	 * @return
	 */
	CODOMAIN evaluate(final DOMAIN parameter);


	default <COCODOMAIN extends Thing>
	Function<? super DOMAIN, ? extends COCODOMAIN> compose(final Function<? super CODOMAIN, ? extends COCODOMAIN> that) {
		return Composition.of(this.evaluate(), that);
	}

	static Function<? super Thing,? extends Thing> Composition(final Stream<Function<? super Thing, ? extends Thing>> composed) {
		return composed.reduce(Function<? super Thing,? extends Thing>::compose).orElse(Function.ID);
	}

	@SafeVarargs
	static Function<? super Thing,? extends Thing> Composition(final Function<? super Thing,? extends Thing>... composed) {
		return Composition(Stream.of(composed));
	}

	@Override
	FunctionType typeof();

	@Override
	Function<? super DOMAIN, ? extends CODOMAIN> evaluate();


	interface Visitor<T, DOMAIN extends Thing, CODOMAIN extends Thing> extends Nothing.Visitor<T> {
		T handle(Function<? super DOMAIN, ? extends CODOMAIN> that);

		default T handle(final Abstraction<DOMAIN, ? extends Thing> that) {
			return handle((Function<? super DOMAIN, ? extends CODOMAIN>)that);
		}
		default T handle(final Composition<? super DOMAIN, ?, ? extends CODOMAIN> that) {
			return handle((Function<? super DOMAIN, ? extends CODOMAIN>)that);
		}
		default T handle(final SystemFunction<? extends Thing, ? extends Thing> that)  {
			return handle((Function<? super DOMAIN, ? extends CODOMAIN>)that);
		}
		default T handle(final UnionFunction<? extends Thing, ? extends Thing> that)  {
			return handle((Function<? super DOMAIN, ? extends CODOMAIN>)that);
		}
		default T handle(final Identity<DOMAIN> that)  {
			return handle((Function<? super DOMAIN, ? extends CODOMAIN>)that);
		}
		default T handle(final EmptyFunction<DOMAIN> that)  {
			return handle((Function<? super DOMAIN, ? extends CODOMAIN>)that);
		}

		@Override
		@SuppressWarnings("unchecked")
		default T handle(final Nothing that) {
			return (T)that;
		}

	}


	@Override
	default  <T> T accept(final Thing.Visitor<T> visitor) {
		return accept(visitor);
	}

	<T> T accept(final Visitor<T,DOMAIN,CODOMAIN> visitor);

}
