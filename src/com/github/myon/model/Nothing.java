package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.function.EmptyFunction;

public abstract class Nothing extends Exception implements Application<Nothing,Nothing>, EmptyFunction<Nothing,Nothing>, Void<Nothing>, Epsilon<Nothing> {

	private final String what;

	/**
	 *
	 */
	private static final long serialVersionUID = 2238019415418108275L;

	public String what() {
		return what;
	}

	Nothing(final String what) {
		this.what = what;
	}

	public static Nothing of(final String what) {
		return new Uncaused(what);
	}

	public static Nothing of(final String what, final Nothing cause) {
		return new Caused(what, cause);
	}

	@Override
	public Nothing implementation() {
		return this;
	}

	static class Uncaused extends Nothing {


		/**
		 *
		 */
		private static final long serialVersionUID = -8326751313963294505L;

		Uncaused(final String what) {
			super(what);
		}

		@Override
		public <T> T accept(final Visitor<T> visitor) {
			return visitor.handle(this);
		}

		@Override
		public String toString() {
			return "§("+what()+")";
		}

	}

	static class Caused extends Nothing {

		/**
		 *
		 */
		private static final long serialVersionUID = 5407820531292548314L;

		Caused(final String what, final Nothing cause) {
			super(what);
			this.cause = cause;
		}

		private final Nothing cause;

		public Nothing cause() {
			return cause;
		}

		@Override
		public <T> T accept(final Visitor<T> visitor) {
			return visitor.handle(this);
		}
		@Override
		public String toString() {
			return "§("+cause.toString()+" -> "+what()+")";
		}

	}

	@Override
	public <T> T accept(final Type.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	@Override
	public <T> T accept(final Epsilon.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	@Override
	public <T> T accept(final Thing.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	@Override
	public <T> T accept(final Function.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}

	abstract <T> T accept(Visitor<T> visitor);

	interface Visitor<T> {
		T handle(final Nothing that);

		default  T handle(final Caused that) {
			return handle((Nothing)that);
		}

	}

	@Override

	public Epsilon isEqual( final Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.of("unequal");
			}
		});
	}


	public static Nothing TypeMiss(final Type type, final Thing thing) {
		return of(thing.toString()+" is not contained in "+type.toString());
	}

	@Override
	public
	Thing parameter() {
		return of("Function 'parameter' is not defined on type 'Nothing'");
	}

	@Override
	public
	Function function() {
		return of("Function 'function' is not defined on type 'Nothing'");
	}


	@Override
	public
	Void typeof() {
		return Void.INSTANCE;
	}


	@Override

	public Nothing evaluate() {
		return this;
	}


	@Override
	public Nothing evaluate( final Thing parameter) {
		return of("Function 'apply' is not defined on type 'Nothing'");
	}

	@Override
	public Type codomain( final Type parameter) {
		return of("Function 'codomain' is not defined on type 'Nothing'");
	}


	@Override
	public boolean isEvaluable() {
		return false;
	}


	@Override
	public Type domain() {
		return of("Function 'domain' is not defined on type 'Nothing'");
	}


	@Override
	public Type codomain() {
		return of("Function 'codomain' is not defined on type 'Nothing'");
	}

	@Override
	public Epsilon invert(final String msg) {
		return Epsilon.INSTANCE;
	}

	@Override
	public Stream<Thing<? extends Nothing>> factors() {
		return Stream.of();
	}

	@Override
	public  Stream<? extends Nothing> superposed() {
		return Stream.of();
	}

	@Override
	public Stream<? extends Thing> threads() {
		return Stream.of();
	}

	@Override
	public Nothing cast(final Thing thing) {
		return Nothing.of("casted!!!");
	}

}
