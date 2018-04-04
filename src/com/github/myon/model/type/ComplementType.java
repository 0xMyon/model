package com.github.myon.model.type;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.Void;

public interface ComplementType extends Type {

	Type complement();

	static Type of(final Type complement) {
		if (complement instanceof ComplementType) {
			return ((ComplementType) complement).complement();
		} else {
			return new ComplementType() {
				@Override
				public  Type complement() {
					return complement;
				}
				@Override
				public String toString() {
					return "!"+complement.toString();
				}
				@Override
				public  <T> T accept(final  Visitor<T> visitor) {
					return visitor.handle(this);
				}
				@Override
				public int compareTo(final Thing that) {
					return that.accept(new Thing.Visitor<Integer>() {
						@Override
						public Integer handle(final Thing that) {
							return getClass().getName().compareTo(that.getClass().getName());
						}
						@Override
						public Integer handle(final ComplementType that) {
							return complement().compareTo(that.complement());
						}
					});
				}
			};
		}
	}

	@Override
	public default boolean isEvaluable() {
		return complement().isEvaluable();
	}

	@Override
	public default  Type evaluate() {
		return of(complement().evaluate());
	}

	@Override
	public default  Epsilon contains( final Thing thing) {
		return thing.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public  Epsilon handle(final  Nothing that) {
				return Epsilon.INSTANCE;
			}
			@Override
			public  Epsilon handle(final  Thing that) {
				return complement().contains(that).invert("Type missmatch: "+complement().toString()+" >>> "+that.toString());
			}
		});
	}

	@Override
	public default  Epsilon containsAll(final  Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public  Epsilon handle(final  Nothing that) {
				return Nothing.of("not a type");
			}
			@Override
			public  Epsilon handle(final  Void that) {
				return Epsilon.INSTANCE;
			}
			@Override
			public  Epsilon handle(final  Type that) {
				return complement().intersetcs(that).invert("Type missmatch: "+complement().toString()+" >>> "+that.toString());
			}
		});
	}

	@Override
	public default  Epsilon intersetcs(final  Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public  Epsilon handle(final  Nothing that) {
				return Nothing.of("no type");
			}

			@Override
			public  Epsilon handle(final  Type that) {
				return complement().containsAll(type).invert("Type missmatch: "+complement().toString()+" >>> "+that.toString());
			}
			@Override
			public  Epsilon handle(final  ComplementType that) {
				return Epsilon.INSTANCE;
			}

		});
	}

}
