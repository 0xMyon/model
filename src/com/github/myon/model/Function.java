package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.function.Abstraction;
import com.github.myon.model.function.Composition;
import com.github.myon.model.function.EmptyFunction;
import com.github.myon.model.function.Identity;
import com.github.myon.model.function.SystemFunction;
import com.github.myon.model.function.UnionFunction;
import com.github.myon.model.type.FunctionType;

public interface Function<THIS extends Function<THIS, DOMAIN, CODOMAIN>, DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>> extends Thing<THIS> {

	static <TYPE extends Thing<TYPE>>
	Function<?,? super TYPE, ? extends TYPE> ID() {
		return null;//Identity.<TYPE>of(Type.ANYTHING);;
	}

	/**
	 * Applies parameters to the function
	 * @param parameter
	 * @return
	 */
	CODOMAIN evaluate(final Thing<? extends DOMAIN> parameter);


	default <COCODOMAIN extends Thing<COCODOMAIN>>
	Function<?,? super DOMAIN, ? extends COCODOMAIN>
	compose(final Function<?,? super CODOMAIN, ? extends COCODOMAIN> that) {
		return Composition.of(this.evaluate(), that);
	}

	static Function Composition(final Stream<Function> composed) {
		return composed.reduce(Function::compose).orElse(Function.ID());
	}

	@SafeVarargs
	static <DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>>
	Function<?,? super DOMAIN,? extends CODOMAIN>
	Composition(final Function<?,? super DOMAIN,? extends CODOMAIN>... composed) {
		return Composition(Stream.of(composed));
	}

	@Override
	FunctionType typeof();

	@Override
	Function<?,? super DOMAIN, ? extends CODOMAIN> evaluate();


	static interface Visitor<T> extends Nothing.Visitor<T> {

		<DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>> T handle(Function<?,? super DOMAIN, ? extends CODOMAIN> that);

		default <DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>>
		T handle(final Abstraction<?,DOMAIN, CODOMAIN> that) {
			return handle((Function<?,? super DOMAIN, ? extends CODOMAIN>)that);
		}
		default <DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>>
		T handle(final Composition<?,? super DOMAIN, ?, ? extends CODOMAIN> that) {
			return handle((Function<?,? super DOMAIN, ? extends CODOMAIN>)that);
		}
		default <DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>>
		T handle(final SystemFunction<?,? super DOMAIN, ? extends CODOMAIN> that)  {
			return handle((Function<?,? super DOMAIN, ? extends CODOMAIN>)that);
		}
		default <DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>>
		T handle(final UnionFunction<?,? super DOMAIN, ? extends CODOMAIN> that)  {
			return handle((Function<?,? super DOMAIN, ? extends CODOMAIN>)that);
		}
		default <DOMAIN extends Thing<DOMAIN>>
		T handle(final Identity<?,DOMAIN> that)  {
			return handle((Function<?,? super DOMAIN, ? extends DOMAIN>)that);
		}
		default <DOMAIN extends Thing<DOMAIN>>
		T handle(final EmptyFunction<?,DOMAIN> that) {
			return handle((Function<?,? super DOMAIN, ? extends DOMAIN>)that);
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

	<T> T accept(final Visitor<T, ? super DOMAIN, ? extends CODOMAIN> visitor);

}
