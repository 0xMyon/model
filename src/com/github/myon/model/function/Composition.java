package com.github.myon.model.function;

import java.util.stream.Stream;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.type.FunctionType;


public interface Composition extends Function {

	Stream<? extends Function> elements();

	@Override
	default Epsilon isEqual(final Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.of("unequal");
			}
			@Override
			public Epsilon handle(final Composition that) {
				return Epsilon.Conjunction();
			}
		});
	}

	static Function of(final Stream<Function> elements) {
		return of(elements.map(t -> t.accept(new Function.Visitor<Stream<? extends Function>>() {
			@Override
			public Stream<Function> handle(final Function that) {
				return Stream.of(that);
			}
			@Override
			public Stream<Function> handle(final Nothing that) {
				return Stream.of(that);
			}
			@Override
			public Stream<? extends Function> handle(final Composition that) {
				return that.elements();
			}
		})).reduce(Stream.of(), Stream::concat).toArray(Function[]::new));
		//return of(elements.toArray(Function[]::new));
	}

	static Function of(final Function... elements) {
		// TODO check of composition is possible
		switch (elements.length) {
		case 0:
			return Nothing.of("no composed functions of 0 elements");
		case 1:
			return elements[0];
		default:
			return new Composition() {
				@Override
				public Stream<Function> elements() {
					return Stream.of(elements);
				}
				@Override
				public <T> T accept(final Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public String toString() {
					return elements().map(Object::toString).reduce((a,b)->a+"."+b).orElse("id");
				}

			};
		}
	}


	@Override
	public default boolean isEvaluable() {
		return elements().anyMatch(Function::isEvaluable);
	}

	@Override
	default FunctionType typeof() {
		return FunctionType.of(
				elements().<Function>map(t->t).findFirst().orElse(Nothing.of("empty")).typeof().domain(),
				(final Type parameter) ->
				elements().map(Function::typeof).<java.util.function.Function<Type,Type>>map(f -> f::codomain )
				.reduce(java.util.function.Function::andThen).orElse(t -> t).apply(parameter)
				);
	}


	@Override
	public
	default Function evaluate() {
		return of(elements().map(Function::evaluate));
	}


	@Override
	public default Thing evaluate(final Thing parameter) {
		return elements().<java.util.function.Function<Thing,Thing>>map(f -> f::evaluate )
				.reduce(java.util.function.Function::andThen).orElse(t -> t).apply(parameter);
	}


}
