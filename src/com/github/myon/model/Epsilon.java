package com.github.myon.model;

import java.util.stream.Stream;

public interface Epsilon extends Product {

	static final  Epsilon INSTANCE = new Epsilon() {
		@Override
		public String toString() {
			return "ɛ";
		}
		@Override
		public <T> T accept(final Epsilon.Visitor<T> visitor) {
			return visitor.handle(this);
		}
	};

	@Override

	default Epsilon isEqual( final Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public  Epsilon handle(final Thing that) {
				return Nothing.of("unequal");
			}
			@Override
			public  Epsilon handle(final Epsilon that) {
				return that;
			}
		});
	}

	static Epsilon of(final boolean b) {
		return b ? INSTANCE : Nothing.of("false");
	}

	@Override
	public
	default Stream<? extends Thing> factors() {
		return Stream.of();
	}

	public default Epsilon invert(final String msg) {
		return Nothing.of(msg);
	}

	@Override
	public default  <T> T accept(final Thing.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}

	<T> T accept(Visitor<T> visitor);

	interface Visitor<T> extends Nothing.Visitor<T> {

		T handle(Epsilon that);


		@Override
		@SuppressWarnings("unchecked")
		default  T handle( final Nothing that) {
			return (T)that;
		}


	}

	/**
	 * Does not have to be overwritten in {@link Nothing}!
	 * @param that
	 * @return
	 */
	default Epsilon conjoin(final Epsilon that) {
		return that.accept(new Visitor<Epsilon>(){
			@Override
			public  Epsilon handle(final Epsilon that) {
				return Epsilon.this;
			}
		});
	}

	/**
	 * Does not have to be overwritten in {@link Nothing}!
	 * @param that
	 * @return
	 */
	default Epsilon disjoin(final Epsilon that) {
		return this;
	}


	static Epsilon Conjunction(final Epsilon... values) {
		return Conjunction(Stream.of(values));
	}


	static Epsilon Conjunction(final Stream<Epsilon> values) {
		return values.reduce(Epsilon.INSTANCE, Epsilon::conjoin);
	}

	static Epsilon Disjunction(final Epsilon... values) {
		return Disjunction(Stream.of(values));
	}

	static Epsilon Disjunction(final Stream<Epsilon> values) {
		return values.reduce(Nothing.of("disjoined"), Epsilon::disjoin);
	}

	@SuppressWarnings("unchecked")
	default <T extends Thing> T branch(final T pos) {
		return branch(pos, (T) Nothing.of(""));
	}

	default <T> T branch(final T pos, final T neg) {
		return accept(new Visitor<T>() {
			@Override
			public T handle(final Epsilon that) {
				return pos;
			}
			@Override
			public T handle(final Nothing that) {
				return neg;
			}
		});
	}

}
