package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.Thing.Visitor;

public interface Nothing extends Application, Function, Void, Epsilon {

	String what();
	
	static Nothing of(String what) {
		return new Nothing() {
			@Override
			public String what() {
				return what;
			}
			@Override
			public String toString() {
				return "§("+what+")";
			}
			@Override
			public <T> T accept(Visitor<T> visitor) {
				return visitor.handle(this);
			}
		};
	}
	
	static Nothing of(String what, Nothing cause) {
		return Caused.of(what, cause);
	}
	
	
	interface Caused extends Nothing {
		Nothing cause();
		static Caused of(String what, Nothing cause) {
			return new Caused() {
				@Override
				public Nothing cause() {
					return cause;
				}
				@Override
				public String what() {
					return what;
				}
				@Override
				public <T> T accept(Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public String toString() {
					return "§("+cause.toString()+" -> "+what+")";
				}
			};
		}
	}
	
	default <T> T accept(Type.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	default <T> T accept(Epsilon.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	default <T> T accept(Thing.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	
	<T> T accept(Visitor<T> visitor); 
	
	interface Visitor<T> {
		T handle(Nothing that);
		
		default T handle(Caused that) {
			return handle((Nothing)that);
		}
		
	}
	

	static Nothing TypeMiss(Type type, Thing thing) {
		return of(thing.toString()+" is not contained in "+type.toString());
	}

	@Override
	public @NonNull
	default Thing parameter() {
		return of("Function 'parameter' is not defined on type 'Nothing'");
	}
	
	@Override
	public @NonNull
	default Function function() {
		return of("Function 'function' is not defined on type 'Nothing'");
	}


	@Override
	default Void typeof() {
		return Void.INSTANCE;
	}


	@Override
	default @NonNull Nothing evaluate() {
		return this;
	}
	

	@Override
	public default Thing apply(@NonNull Thing parameter) {
		return of("Function 'apply' is not defined on type 'Nothing'");
	}

	@Override
	public default Type codomain(@NonNull Type parameter) {
		return of("Function 'codomain' is not defined on type 'Nothing'");
	}


	@Override
	default boolean isEvaluable() {
		return false;
	}


	@Override
	default Type domain() {
		return of("Function 'domain' is not defined on type 'Nothing'");
	}


	@Override
	default Type codomain() {
		return of("Function 'codomain' is not defined on type 'Nothing'");
	}

	@Override
	public default Epsilon invert(String msg) {
		return Epsilon.INSTANCE;
	}

	@Override
	default @NonNull Nothing[] factors() {
		return new Nothing[0];
	}
	
}
