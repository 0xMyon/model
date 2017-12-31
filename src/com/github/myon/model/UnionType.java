package com.github.myon.model;

import java.util.stream.Stream;

public interface UnionType extends Type {

	Stream<? extends Type> summants();

	static Type of(final Stream<Type> summants) {
		return of(summants.toArray(Type[]::new));
	}

	static Type of(final  Type... summants) {
		switch (summants.length) {
		case 0:
			return Void.INSTANCE;
		case 1:
			return summants[0];
		default:
			return new UnionType() {
				@Override
				public  Stream<Type> summants() {
					return Stream.of(summants);
				}
				@Override
				public <T>  T accept( final Visitor<T> visitor) {
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
	default  Type evaluate() {
		return of(summants().map(Type::evaluate));
	}

	@Override
	default  Epsilon intersetcs( final Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default  Epsilon containsAll( final Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default  Epsilon contains( final Thing thing) {
		return Epsilon.Disjunction(summants().map(t -> t.contains(thing)));
	}

}
