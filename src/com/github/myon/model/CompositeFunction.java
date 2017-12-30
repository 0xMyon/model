package com.github.myon.model;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;


public interface CompositeFunction extends Function {
	
	Stream<Function> elements();
	
	@NonNull
	default Epsilon isEqual(@NonNull Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Thing that) {
				return Nothing.of("unequal");
			}
			@Override
			public Epsilon handle(CompositeFunction that) {
				return Epsilon.Conjunction();
			}
		});
	}
	
	static Function of(Stream<Function> elements) {
		return of(elements.toArray(Function[]::new));
	}
	
	static Function of(Function... elements) {
		// TODO check of composition is possible
		switch (elements.length) {
		case 0:
			return Nothing.of("no composed functions of 0 elements");
		case 1:
			return elements[0];
		default:
			return new CompositeFunction() {
				@Override
				public Stream<Function> elements() {
					return Stream.of(elements);
				}
				@Override
				public <T> T accept(Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public String toString() {
					return elements().map(Object::toString).reduce("", (a,b)->a+"."+b);
				}
			};
		}
	}
	

	@Override
	public default boolean isEvaluable() {
		return elements().anyMatch(Function::isEvaluable);
	}
	

	@Override
	public default Type domain() {
		// TODO maybe return void
		return elements().findFirst().orElse(Nothing.of("empty")).domain();
	}
	
	@Override
	public @NonNull
	default Function evaluate() {
		return of(elements().map(Function::evaluate));
	}
	
	//TODO extract reduce
	
	@Override
	public default Type codomain(@NonNull Type parameter) {
		return elements().<java.util.function.Function<Type,Type>>map(f -> f::codomain )
				.reduce(java.util.function.Function::andThen).orElse(t -> t).apply(parameter);
	}
	
	@Override
	public default Thing apply(@NonNull Thing parameter) {
		return elements().<java.util.function.Function<Thing,Thing>>map(f -> f::apply )
				.reduce(java.util.function.Function::andThen).orElse(t -> t).apply(parameter);
	}
	

}
