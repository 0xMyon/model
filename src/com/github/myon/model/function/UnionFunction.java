package com.github.myon.model.function;

import java.util.stream.Stream;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Superposition;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.type.FunctionType;

import util.Streams;


public interface UnionFunction<THIS extends UnionFunction<THIS, DOMAIN, CODOMAIN>, DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>> extends Function<THIS,DOMAIN, CODOMAIN> {

	Stream<? extends Function<?,? super DOMAIN, ? extends CODOMAIN>> superposed();


	interface I<DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>> extends UnionFunction<I<DOMAIN,CODOMAIN>, DOMAIN, CODOMAIN> {

		@Override
		default I<DOMAIN,CODOMAIN> THIS() {
			return this;
		}

		@Override
		default I<DOMAIN,CODOMAIN> evaluate() {
			return of(superposed().map(Function::evaluate));
		}

	}


	static <DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>>
	Function<?,? super DOMAIN, ? extends CODOMAIN>
	of(final Stream<Function<?,? super DOMAIN, ? extends CODOMAIN>> summants) {
		return of(summants.toArray(Function[]::new));
	}



	@SafeVarargs
	static <DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>>
	Function<?,? super DOMAIN, ? extends CODOMAIN> of(final  Function<?,? super DOMAIN, ? extends CODOMAIN>... summants) {
		switch (summants.length) {
		case 0:
			throw new Error("return empty function here");
		case 1:
			return summants[0];
		default:
			return new I<DOMAIN, CODOMAIN>() {
				@Override
				public  Stream<Function<?,? super DOMAIN, ? extends CODOMAIN>> superposed() {
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
				@Override
				public FunctionType typeof() {
					return FunctionType.of(
							superposed().map(Function::typeof).map(FunctionType::domain).reduce(Type::intersect).orElse(Type.ANYTHING),
							parameter -> superposed().map(Function::typeof).map(f -> f.codomain(parameter)).reduce(Type::unite).orElse(Type.VOID)
							);
				}
			};
		}
	}

	@Override
	default boolean isEvaluable() {
		return superposed().anyMatch(Function::isEvaluable);
	}

	@Override
	default Epsilon<?> isEqual( final Thing<?> that) {
		return that.accept(new Thing.Visitor<Epsilon<?>>(){
			@Override
			public  Epsilon<?> handle(final Thing<?> that) {
				return Nothing.of("not equal");
			}
			@Override
			public Epsilon<?> handle(final UnionFunction<?,?,?> that) {
				try {
					return Epsilon.Conjunction(Streams.zip(superposed(), that.superposed(), Function::isEqual));
				} catch (final Nothing e) {
					return e;
				}
			}
		});
	}



	@Override
	default Thing<? extends CODOMAIN> evaluate(final Thing<? extends DOMAIN> parameter) {
		return Superposition.of(superposed().map(f -> f.evaluate(parameter)));
	}

}
