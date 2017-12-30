package com.github.myon.model;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;


public interface UnionType extends Type {

	@NonNull Stream<? extends Type> summants();
	
	static Type of(Stream<Type> summants) {
		return of(summants.toArray(Type[]::new));
	}
	
	static Type of(Type... summants) {
		switch (summants.length) {
		case 0:
			return Void.INSTANCE;
		case 1:
			return summants[0];
		default:
			return new UnionType() {
				@Override
				public @NonNull Stream<Type> summants() {
					return Stream.of(summants);
				}
				@Override
				public <T> @NonNull T accept(@NonNull Visitor<T> visitor) {
					return visitor.handle(this);
				}
			};
		}
	}
	
	
	@Override
	default boolean isEvaluable() {
		return summants().anyMatch(Type::isEvaluable);
	}
	
	@Override
	default @NonNull Type evaluate() {
		return of(summants().map(Type::evaluate));
	}

	@Override
	default @NonNull Epsilon intersetcs(@NonNull Type type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	default @NonNull Epsilon containsAll(@NonNull Type type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	default @NonNull Epsilon contains(@NonNull Thing thing) {
		return Epsilon.Disjunction(summants().map(t -> t.contains(thing)));
	}
	
}
