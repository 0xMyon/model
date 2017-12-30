package com.github.myon.model;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.function.SystemType;

public interface Union extends Thing {

	@NonNull Stream<? extends Thing> summants();
	
	static @NonNull Thing of(final @NonNull Stream<Thing> summants) {
		return of(summants.toArray(Thing[]::new));
	}
		
	
	static @NonNull Thing of(final @NonNull Thing... summants) {
		switch (summants.length) {
		case 0:
			return Nothing.of("empty union");
		case 1:
			return summants[0];
		default:
			return new Union() {
				@Override
				public <T> @NonNull T accept(@NonNull Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public @NonNull Stream<Thing> summants() {
					return Stream.of(summants);
				}
			};
		}
	}
	
	@Override
	default @NonNull Type typeof() {
		return summants().map(Thing::typeof).reduce(SystemType.VOID, Type::unite);
	}
	
	@Override
	default boolean isEvaluable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	default @NonNull Thing evaluate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	default @NonNull Epsilon isEqual(@NonNull Thing that) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
