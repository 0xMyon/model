package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.function.Abstraction;
import com.github.myon.model.function.Composition;
import com.github.myon.model.function.Identity;
import com.github.myon.model.function.SystemFunction;
import com.github.myon.model.function.UnionFunction;
import com.github.myon.model.type.FunctionType;

public interface Function extends Thing {

	static final Function ID = Identity.of(Type.ANYTHING);

	/**
	 * Applies parameters to the function
	 * @param parameter
	 * @return
	 */
	Thing evaluate(Thing parameter);

	Type domain();

	default Type codomain() {
		return codomain(domain());
	}

	Type codomain(Type parameter);


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
	public default FunctionType typeof() {
		return FunctionType.of(domain(), this::codomain);
	}

	@Override
	Function evaluate();

	interface Visitor<T> extends Nothing.Visitor<T> {
		T handle(Function that);

		default T handle(final Abstraction that) {
			return handle((Function)that);
		}
		default T handle(final Composition that) {
			return handle((Function)that);
		}
		default T handle(final SystemFunction that)  {
			return handle((Function)that);
		}
		default T handle(final UnionFunction that)  {
			return handle((Function)that);
		}
		default T handle(final Identity that)  {
			return handle((Function)that);
		}

		@Override
		@SuppressWarnings("unchecked")
		default T handle(final Nothing that) {
			return (T)that;
		}

	}

	@Override
	default  <T> T accept(final Thing. Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}

	<T> T accept(Visitor<T> visitor);

}
