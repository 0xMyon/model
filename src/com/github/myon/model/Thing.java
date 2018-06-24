package com.github.myon.model;

/**
 * Most abstract type
 * @author 0xMyon
 */
public interface Thing<THIS extends Thing<THIS>> {

	/**
	 * @return most fitting {@link Type}
	 */
	Type<?> typeof();

	/**
	 * equality
	 * @param that
	 * @return
	 */
	Epsilon<?> isEqual(final Thing<?> that);


	Thing<? extends THIS> evaluate();

	THIS THIS();


	default <CODOMAIN extends Thing<CODOMAIN>>
	Thing<? extends CODOMAIN> apply(final Function<?,? super THIS, ? extends CODOMAIN> function) {
		return Application.<THIS,CODOMAIN>of(function, THIS());
	}

	boolean isEvaluable();

	default void printEval() {
		Thing<?> thing = this;
		System.out.println(thing.toString());
		int i = 0;
		while(thing.isEvaluable() && (i++ < 100)) {
			thing = thing.evaluate();
			System.out.println("> "+thing.toString());
		}
	}


	/**
	 * Superposes two {@link Thing}s
	 * @param that left hand operand
	 * @return Superposed {@link Thing}
	 */
	default Thing superpose(final Thing that) {
		return Superposition.of(this, that);
	}

	/**
	 * Superposes multiple {@link Thing}s
	 * @param supoersosed
	 * @return
	 * @see #superpose(Thing)
	 */

	/*
	static Thing Superposition(final Stream<Thing> supoersosed) {
		return supoersosed.reduce(Thing::superpose).orElse(Nothing.of("empty superposition"));
	}

	static Thing Superposition(final Thing... superposed) {
		return Superposition(Stream.of(superposed));
	}


	default Thing<?> concat(final Thing<?> that) {
		return Product.of(this, that);
	}

	static Thing Sequence(final Stream<Thing> sequenced) {
		return sequenced.reduce(Epsilon.INSTANCE, Thing::concat);
	}
	 */


	<T> T accept(final Visitor<T> visitor);

	static interface Visitor<T> extends Type.Visitor<T>, Epsilon.Visitor<T>, Function.Visitor<T> {

		T handle(final Thing<?> that);

		@Override
		default T handle(final Type<?> that) {
			return handle((Thing<?>)that);
		}

		@Override
		default <DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>>
		T handle(final Function<?,? super DOMAIN,? extends CODOMAIN> that)  {
			return handle((Thing<?>)that);
		}

		@Override
		default T handle(final Epsilon<?> that) {
			return handle((Product<?,?>)that);
		}

		default T handle(final Product<?,?> that) {
			return handle((Thing<?>)that);
		}

		default T handle(final Superposition<?,?> that) {
			return handle((Thing<?>)that);
		}

		default T handle(final Concurrency<?,?> that) {
			return handle((Thing<?>)that);
		}

		default T handle(final Application<?,?> that)  {
			return handle((Thing<?>)that);
		}

		@Override
		@SuppressWarnings("unchecked")
		default T handle(final Nothing that) {
			return (T)that;
		}

	}


}
