package com.github.myon.model;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

public abstract class Nothing extends Exception implements Application, Function, Void, Epsilon, Abstraction, Union, UnionFunction {

	private String what;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2238019415418108275L;

	String what() {
		return what;
	}
	
	Nothing(String what) {
		this.what = what;
	}
	
	public static Nothing of(String what) {
		return new Uncaused(what);
	}
	
	public static Nothing of(String what, Nothing cause) {
		return new Caused(what, cause);
	}
	
	@Override
	public Function implementation() {
		return this;
	}
	
	
	static class Uncaused extends Nothing {
		

		/**
		 * 
		 */
		private static final long serialVersionUID = -8326751313963294505L;

		Uncaused(String what) {
			super(what);
		}
		
		@Override
		public <T> T accept(Visitor<T> visitor) {
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

		Caused(String what, Nothing cause) {
			super(what);
			this.cause = cause;
		}
		
		private Nothing cause;
		
		public Nothing cause() {
			return cause;
		}
		
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.handle(this);
		}
		@Override
		public String toString() {
			return "§("+cause.toString()+" -> "+what()+")";
		}

	}
	
	public <T> T accept(Type.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	public <T> T accept(Epsilon.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	public <T> T accept(Thing.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	public <T> T accept(Function.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	
	abstract <T> T accept(Visitor<T> visitor); 
	
	interface Visitor<T> {
		T handle(Nothing that);
		
		default T handle(Caused that) {
			return handle((Nothing)that);
		}
		
	}
	
	@NonNull
	public Epsilon isEqual(@NonNull Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Thing that) {
				return Nothing.of("unequal");
			}
		});
	}
	

	static Nothing TypeMiss(Type type, Thing thing) {
		return of(thing.toString()+" is not contained in "+type.toString());
	}

	@Override
	public @NonNull
	 Thing parameter() {
		return of("Function 'parameter' is not defined on type 'Nothing'");
	}
	
	@Override
	public @NonNull
	 Function function() {
		return of("Function 'function' is not defined on type 'Nothing'");
	}


	@Override
	public
	 Void typeof() {
		return Void.INSTANCE;
	}


	@Override
	 @NonNull
	public Nothing evaluate() {
		return this;
	}
	

	@Override
	public Thing apply(@NonNull Thing parameter) {
		return of("Function 'apply' is not defined on type 'Nothing'");
	}

	@Override
	public Type codomain(@NonNull Type parameter) {
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
	public Epsilon invert(String msg) {
		return Epsilon.INSTANCE;
	}

	@Override
	public @NonNull Stream<? extends Nothing> factors() {
		return Stream.of();
	}
	
	@Override
	public @NonNull Stream<? extends Nothing> summants() {
		return Stream.of();
	}
	
}
