package com.github.myon.model;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.Thing.Visitor;

public interface ProductType extends Type {


	Type[] factors();

	static Type create(final Type... factors) {
		switch (factors.length) {
		case 1:
			return factors[0];
		default:
			return new ProductType() {
				@Override
				public Type[] factors() {
					return factors;
				}
				public String toString() {
					return Stream.of(factors).map(Object::toString).reduce((a,b)->a+" ** "+b).orElse("[()]");
				}
				@Override
				public <T> T accept(Visitor<T> visitor) {
					return visitor.handle(this);
				}
			};
		}
	}


	@Override
	public default boolean isEvaluable() {
		return Stream.of(factors()).anyMatch(Type::isEvaluable);
	}

	@Override
	public @NonNull	default Type evaluate() {
		return create(Stream.of(factors()).map(Type::evaluate).toArray(Type[]::new));
	}

	@Override
	public default Epsilon intersetcs(final Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {

			@Override
			public Epsilon handle(Nothing that) {
				return that;
			}

			@Override
			public Epsilon handle(Type that) {
				return Nothing.of("doeas not intersect");
			}
			
			@Override
			public Epsilon handle(ProductType that) {
				if (factors().length == that.factors().length) {
					Epsilon[] epsilons = new Epsilon[Math.min(factors().length, that.factors().length)];
					Arrays.setAll(epsilons, i -> factors()[i].intersetcs(that.factors()[i]));
					return Epsilon.conjunction(epsilons);
				} else {
					return Nothing.of("ProductTypes have different length");
				}
			}
		
		});
	}



	@Override
	public default Epsilon containsAll(final Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Nothing that) {
				return Nothing.of(that.toString()+" is not a type");
			}
			@Override
			public Epsilon handle(Type that) {
				return Nothing.of(that.toString()+" is no subtype");
			}
			@Override
			public Epsilon handle(ProductType that) {
				if (factors().length == that.factors().length) {
					Epsilon[] epsilons = new Epsilon[Math.min(factors().length, that.factors().length)];
					Arrays.setAll(epsilons, i -> factors()[i].containsAll(that.factors()[i]));
					return Epsilon.conjunction(epsilons);
				} else {
					return Nothing.of("ProductTypes have different length");
				}
			}
			
		});
	}

	@Override
	public default Epsilon contains(@NonNull final Thing thing) {
		return thing.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Thing that) {
				return Nothing.TypeMiss(ProductType.this, thing);
			}
			@Override
			public Epsilon handle(Nothing that) {
				return Epsilon.INSTANCE;
			}
		});
	}

}
