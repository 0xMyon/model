package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.function.SystemType;

public interface Union extends Thing {

	Stream<? extends Thing> summants();

	static Thing of(final Stream<Thing> summants) {
		return of(summants.toArray(Thing[]::new));
	}


	static Thing of(final  Thing... summants) {
		switch (summants.length) {
		case 0:
			return Nothing.of("empty union");
		case 1:
			return summants[0];
		default:
			return new Union() {
				@Override
				public <T>  T accept( final Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public  Stream<Thing> summants() {
					return Stream.of(summants);
				}
			};
		}
	}

	@Override
	default  Type typeof() {
		return summants().map(Thing::typeof).reduce(SystemType.VOID, Type::unite);
	}

	@Override
	default boolean isEvaluable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default  Thing evaluate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default  Epsilon isEqual( final Thing that) {
		// TODO Auto-generated method stub
		return null;
	}



}
