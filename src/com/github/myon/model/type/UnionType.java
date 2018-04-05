package com.github.myon.model.type;

import java.util.stream.Stream;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.Void;

public interface UnionType extends Type {

	Stream<? extends Type> superposed();

	static Type of(final Stream<Type> summants) {
		return of(summants.map(t -> t.accept(new Type.Visitor<Stream<? extends Type>>() {
			@Override
			public Stream<Type> handle(final Type that) {
				return Stream.of(that);
			}
			@Override
			public Stream<Type> handle(final Void that) {
				return Stream.of();
			}
			@Override
			public Stream<? extends Type> handle(final UnionType that) {
				return that.superposed();
			}
		})).reduce(Stream.of(), Stream::concat).toArray(Type[]::new));
	}

	static Type of(final Type... summants) {
		switch (summants.length) {
		case 0:
			return Void.INSTANCE;
		case 1:
			return summants[0];
		default:
			return new UnionType() {
				@Override
				public Stream<Type> superposed() {
					return Stream.of(summants).distinct();
				}
				@Override
				public <T> T accept(final Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public String toString() {
					return "("+superposed().map(Thing::toString).reduce("", (a,b)->a+"++"+b)+")";
				}
			};
		}
	}


	@Override
	default boolean isEvaluable() {
		return superposed().anyMatch(Type::isEvaluable);
	}

	@Override
	default Type evaluate() {
		return of(superposed().map(Type::evaluate));
	}

	@Override
	default Epsilon intersetcs(final Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Epsilon containsAll(final Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Epsilon contains(final Thing thing) {
		return Epsilon.Disjunction(superposed().map(t -> t.contains(thing)));
	}

}
