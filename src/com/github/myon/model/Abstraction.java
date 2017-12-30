package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface Abstraction extends Function {

	Type domain();
	Function implementation();
	
	static Abstraction of(Type domain, Function implementation) {
		// TODO maybe evaluate types a compile time
		return implementation.typeof().domain().containsAll(domain).accept(new Epsilon.Visitor<Abstraction>() {
			@Override
			public Abstraction handle(Nothing that) {
				return that;
			}
			@Override
			public Abstraction handle(Epsilon that) {
				return new Abstraction() {
					@Override
					public Function implementation() {
						return implementation;
					}
					@Override
					public Type domain() {
						return domain;
					}
					@Override
					public <T> T accept(Function.Visitor<T> visitor) {
						return visitor.handle(this);
					}
				};
			}
		
		});
	}
	
	@NonNull
	default Epsilon isEqual(@NonNull Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Thing that) {
				return Nothing.of("unequal");
			}
			@Override
			public Epsilon handle(Abstraction that) {
				return Epsilon.Conjunction(
						domain().isEqual(that.domain()), 
						implementation().isEqual(that.implementation())
						);
			}
		});
	}
	
	@Override
	default boolean isEvaluable() {
		return domain().isEvaluable() || implementation().isEvaluable();
	}
	
	
	
	@Override
	default @NonNull Function evaluate() {
		return of(domain().evaluate(), implementation().evaluate());
	}
	
	@Override
	default @NonNull Type codomain(@NonNull Type parameter) {
		return implementation().codomain(parameter);
	}
	
	@Override
	default @NonNull Thing apply(@NonNull Thing parameter) {
		return implementation().apply(parameter);
	}
	
}
