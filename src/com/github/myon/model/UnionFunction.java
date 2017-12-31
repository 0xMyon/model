package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.function.SystemType;

public interface UnionFunction extends Function {

	Stream<? extends Function> summants();

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
				public  Stream<Function> summants() {
					//TODO sort & eliminate doubles & reduce nested Unions
					return Stream.of(summants);
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
	public default boolean isEvaluable() {
		return summants().anyMatch(Function::isEvaluable);
	}

	@Override
	public
	default Epsilon isEqual( final Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>(){
			@Override
			public  Epsilon handle( final Thing that) {
				return Nothing.of("not equal");
			}

		});
	}

	@Override
	public
	default Function evaluate() {
		return of(summants().map(Function::evaluate));
	}

	@Override
	public
	default Type domain() {
		return summants().map(Function::domain).reduce(SystemType.ANYTHING, Type::intersect);
	}

	@Override
	public
	default Type codomain( final Type parameter) {
		return summants().map(f -> f.codomain(parameter)).reduce(SystemType.VOID, Type::unite);
	}

	@Override
	public
	default Thing apply( final Thing parameter) {
		return Union.of(summants().map(f -> f.apply(parameter)));
	}



}
