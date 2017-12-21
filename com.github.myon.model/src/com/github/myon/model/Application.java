package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Application of a {@link Function} to a {@link Thing} parameter
 * @author 0xMyon
 *
 */
public interface Application extends Thing {

	@NonNull Function function();
	@NonNull Thing parameter();
	
	
	static Application create(Function function, Thing parameter) {
		if (function.typeof().domain().containsAll(parameter.typeof())) {
			return new Application() {
				@Override
				public @NonNull Thing parameter() {
					return parameter;
				}
				
				@Override
				public @NonNull Function function() {
					return function;
				}
				public String toString() {
					return function().toString()+"("+parameter().toString()+")";
				}
			};
		} else {
			return Nothing.create("");
		}
	}
	

	@Override
	public @NonNull
	default Type typeof() {
		return function().apply(parameter().typeof());
	}
	
	@Override
	public default boolean isEvaluable() {
		return true;
	}
	
	@Override
	public default Thing evaluate() {
		return function().apply(parameter());
	}
	
}
