package com.github.myon.model.type;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.Void;

public interface ComplementType<THIS extends ComplementType<THIS, COMPLEMENT>, COMPLEMENT extends Type<COMPLEMENT>> extends ElementType<THIS> {

	Type<? extends COMPLEMENT> complement();

	interface I<COMPLEMENT extends Type<COMPLEMENT>> extends ComplementType<I<COMPLEMENT>, COMPLEMENT> {
		@Override
		public default I<COMPLEMENT> evaluate() {
			return of(complement().evaluate());
		}
	}

	static <COMPLEMENT extends Type<COMPLEMENT>> Type<?> of(final Type<? extends COMPLEMENT> complement) {
		return complement.accept(new Visitor<Type>() {
			@Override
			public Type handle(final ComplementType that) {
				return that.complement();
			}
			@Override
			public Type handle(final Type that) {
				return new I<COMPLEMENT>() {
					@Override
					public Type<? extends COMPLEMENT> complement() {
						return that;
					}
					@Override
					public String toString() {
						return "!"+that.toString();
					}
					@Override
					public <T> T accept(final Visitor<T> visitor) {
						return visitor.handle(this);
					}
				};
			}
		});
	}

	@Override
	public default boolean isEvaluable() {
		return complement().isEvaluable();
	}



	@Override
	public default Epsilon<?> contains(final Thing<?> thing) {
		return thing.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Nothing that) {
				return Epsilon.INSTANCE;
			}
			@Override
			public  Epsilon handle(final Thing that) {
				return complement().contains(that).invert("Type missmatch: "+complement().toString()+" >>> "+that.toString());
			}
		});
	}

	@Override
	public default Epsilon<?> containsAll(final Type<?> type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public  Epsilon handle(final Nothing that) {
				return Nothing.of("not a type");
			}
			@Override
			public  Epsilon handle(final Void that) {
				return Epsilon.INSTANCE;
			}
			@Override
			public  Epsilon handle(final Type that) {
				return complement().intersetcs(that).invert("Type missmatch: "+complement().toString()+" >>> "+that.toString());
			}
		});
	}

	@Override
	public default Epsilon<?> intersetcs(final Type<?> type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Nothing that) {
				return Nothing.of("no type");
			}
			@Override
			public Epsilon handle(final Type that) {
				return complement().containsAll(type).invert("Type missmatch: "+complement().toString()+" >>> "+that.toString());
			}
			@Override
			public Epsilon handle(final ComplementType that) {
				return Epsilon.INSTANCE;
			}
		});
	}

}
