package com.github.myon.model;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.function.SystemType;

public interface UnionFunction extends Function {

	@NonNull Stream<? extends Function> summants();
	
	static @NonNull Function of(final @NonNull Stream<Function> summants) {
		return of(summants.toArray(Function[]::new));
	}
		
	
	static @NonNull Function of(final @NonNull Function... summants) {
		switch (summants.length) {
		case 0:
			throw new Error("return empty function here");
		case 1:
			return summants[0];
		default:
			return new UnionFunction() {
				@Override
				public @NonNull Stream<Function> summants() {
					//TODO sort & eliminate doubles & reduce nested Unions
					return Stream.of(summants);
				}
				@Override
				public <T> T accept(Visitor<T> visitor) {
					return visitor.handle(this);
				}
				public String toString() {
					return "";
				}
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
	public @NonNull
	default Epsilon isEqual(@NonNull Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>(){
			@Override
			public @NonNull Epsilon handle(@NonNull Thing that) {
				return Nothing.of("not equal");
			}
			
		});
	}
	
	@Override
	public @NonNull
	default Function evaluate() {
		return of(summants().map(Function::evaluate));
	}
	
	@Override
	public @NonNull
	default Type domain() {
		return summants().map(Function::domain).reduce(SystemType.ANYTHING, Type::intersect);
	}
	
	@Override
	public @NonNull
	default Type codomain(@NonNull Type parameter) {
		return summants().map(f -> f.codomain(parameter)).reduce(SystemType.VOID, Type::unite);
	}
	
	@Override
	public @NonNull
	default Thing apply(@NonNull Thing parameter) {
		return Union.of(summants().map(f -> f.apply(parameter)));
	}
	
	
	
}
