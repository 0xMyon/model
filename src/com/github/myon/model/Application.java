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


	static Application create(final Function function, final Thing parameter) {
		return function.typeof().domain().containsAll(parameter.typeof()).accept(new Epsilon.Visitor<Application>() {

			@Override
			public Application handle(Nothing cause) {
				return Nothing.of("Type missmatch "+function.typeof().domain().toString()+" !>>> "+parameter.typeof().toString(), cause);
			}

			@Override
			public Application handle(Epsilon that) {
				return new Application() {
					@Override
					public @NonNull Thing parameter() {
						return parameter;
					}
		
					@Override
					public @NonNull Function function() {
						return function;
					}
					@Override
					public String toString() {
						return function().toString()+"("+parameter().toString()+")";
					}

					@Override
					public <T> T accept(Visitor<T> visitor) {
						return visitor.handle(this);
					}
				};
			}
		
		
		});
	}


	@Override
	public @NonNull
	default Type typeof() {
		return function().codomain(parameter().typeof());
	}

	@Override
	public default boolean isEvaluable() {
		return true;
	}

	@Override
	public default Thing evaluate() {
		if (function().isEvaluable()) {
			return create(function().evaluate(), parameter());
		} else if (parameter().isEvaluable()) {
			return create(function(), parameter().evaluate());
		} else {
			return function().apply(parameter());
		}
	}

}
