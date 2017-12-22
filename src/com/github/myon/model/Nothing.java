package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface Nothing extends Application, Function, Void {

	String what();
	
	
	static Nothing create(String what) {
		return new Nothing() {
			@Override
			public String what() {
				return what;
			}
			@Override
			public String toString() {
				return "§("+what+")";
			}
		};
	}

	@Override
	public @NonNull
	default Thing parameter() {
		return create("failed");
	}
	
	@Override
	public @NonNull
	default Function function() {
		return create("called");
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
		return create("called");
	}

	@Override
	public default Type codomain(@NonNull Type parameter) {
		return create("called");
	}


	@Override
	default boolean isEvaluable() {
		return false;
	}


	@Override
	default Type domain() {
		return create("called");
	}


	@Override
	default Type codomain() {
		return create("called");
	}
	
}
