package com.github.myon.model.function;

import java.util.stream.Stream;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Streams;
import com.github.myon.model.Superposition;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;


public interface UnionFunction extends Function {

	Stream<? extends Function> superposed();

	static Function of(final Stream<Function> summants) {
		return of(summants.toArray(Function[]::new));
	}

	static Function of(final  Function... summants) {
		switch (summants.length) {
		case 0:
			throw new Error("return empty function here");
		case 1:
			return summants[0];
		default:
			return new UnionFunction() {
				@Override
				public  Stream<Function> superposed() {
					//TODO sort & eliminate doubles & reduce nested Unions
					return Stream.of(summants).distinct();
				}
				@Override
				public <T> T accept(final Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public String toString() {
					return "";
				}
				@Override
				public int hashCode() {
					return Stream.of(summants).mapToInt(Function::hashCode).reduce(Function.class.hashCode(), (a,b) -> a^b);
				}
			};
		}
	}

	@Override
	default boolean isEvaluable() {
		return superposed().anyMatch(Function::isEvaluable);
	}

	@Override
	default Epsilon isEqual( final Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>(){
			@Override
			public  Epsilon handle(final Thing that) {
				return Nothing.of("not equal");
			}
			@Override
			public  Epsilon handle(final UnionFunction that) {
				try {
					return Epsilon.Conjunction(Streams.zip(superposed(), that.superposed(), Function::isEqual));
				} catch (final Nothing e) {
					return e;
				}
			}
		});
	}

	@Override
	default Function evaluate() {
		return of(superposed().map(Function::evaluate));
	}

	@Override
	default Type domain() {
		return superposed().map(Function::domain).reduce(Type.ANYTHING, Type::intersect);
	}

	@Override
	default Type codomain( final Type parameter) {
		return superposed().map(f -> f.codomain(parameter)).reduce(Type.VOID, Type::unite);
	}

	@Override
	default Thing evaluate( final Thing parameter) {
		return Superposition.of(superposed().map(f -> f.evaluate(parameter)));
	}

}
