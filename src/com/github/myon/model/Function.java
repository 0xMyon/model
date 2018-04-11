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

	static final Function ID = Identity.of(Type.ANYTHING);

	/**
	 * Applies parameters to the function
	 * @param parameter
	 * @return
	 */
	CODOMAIN evaluate(final DOMAIN parameter);

	default Function compose(final Function that) {
		return Composition.of(this, that);
	}
	static Function Composition(final Stream<Function> composed) {
		return composed.reduce(Function::compose).orElse(Function.ID);
	}
	static Function Composition(final Function... composed) {
		return Composition(Stream.of(composed));
	}

	@Override
	FunctionType typeof();

	@Override
	Function<? super DOMAIN, ? extends CODOMAIN> evaluate();

	interface Visitor<T> extends Nothing.Visitor<T> {
		T handle(Function<? extends Thing, ? extends Thing> that);

		default T handle(final Abstraction<? extends Thing, ? extends Thing> that) {
			return handle((Function<? extends Thing, ? extends Thing>)that);
		}
		default T handle(final Composition<? extends Thing, ? extends Thing, ? extends Thing> that) {
			return handle((Function<? extends Thing, ? extends Thing>)that);
		}
		default T handle(final SystemFunction<? extends Thing, ? extends Thing> that)  {
			return handle((Function<? extends Thing, ? extends Thing>)that);
		}
		default T handle(final UnionFunction<? extends Thing, ? extends Thing> that)  {
			return handle((Function<? extends Thing, ? extends Thing>)that);
		}
		default T handle(final Identity<? extends Thing> that)  {
			return handle((Function<? extends Thing, ? extends Thing>)that);
		}
		default T handle(final EmptyFunction<? extends Thing> that)  {
			return handle((Function<? extends Thing, ? extends Thing>)that);
		}

		@Override
		@SuppressWarnings("unchecked")
		default T handle(final Nothing that) {
			return (T)that;
		}

	}

	@Override
	default  <T> T accept(final Thing.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}

	<T> T accept(final Visitor<T> visitor);

}
