package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface ComplementType extends Type {

	Type complement();
	
	static Type of(Type complement) {
		if (complement instanceof ComplementType) {
			return ((ComplementType) complement).complement();
		} else {
			return new ComplementType() {
				@Override
				public Type complement() {
					return complement;
				}
				@Override
				public String toString() {
					return "!"+complement.toString();
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
		return complement().isEvaluable();
	}
	
	@Override
	public default Type evaluate() {
		return of(complement().evaluate());
	}
	
	@Override
	public default Epsilon contains(@NonNull Thing thing) {
		return thing.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Nothing that) {
				return Epsilon.INSTANCE;
			}
			@Override
			public Epsilon handle(Thing that) {
				return complement().contains(that).invert("Type missmatch: "+complement().toString()+" >>> "+that.toString());
			}
		});
	}
	
	@Override
	public default Epsilon containsAll(Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Nothing that) {
				return Nothing.of("not a type");
			}
			@Override
			public Epsilon handle(Void that) {
				return Epsilon.INSTANCE;
			}
			@Override
			public Epsilon handle(Type that) {
				return complement().intersetcs(that).invert("Type missmatch: "+complement().toString()+" >>> "+that.toString());
			}
		});
	}
	
	@Override
	public default Epsilon intersetcs(Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Nothing that) {
				return Nothing.of("no type");
			}

			@Override
			public Epsilon handle(Type that) {
				return complement().containsAll(type).invert("Type missmatch: "+complement().toString()+" >>> "+that.toString());
			}
			@Override
			public Epsilon handle(ComplementType that) {
				return Epsilon.INSTANCE;
			}
		
		});
	}
	
}
