package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.Thing.Visitor;

public interface Void extends FunctionType, MetaType, ProductType {

	Void INSTANCE = new Void() {
		@Override
		public String toString() {
			return "{}";
		}
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.handle(this);
		}
	};
	
	@Override
	public default Type[] factors() {
		return new Type[0];
	}
	
	@Override
	public default Nothing base() {
		return Nothing.of("undefined");
	}
	
	@Override
	public default Type domain() {
		return Nothing.of("undefined");
	}
	
	@Override
	public default Type codomain() {
		return Nothing.of("undefined");
	}

	@Override
	default boolean isEvaluable() {
		return false;
	}

	@Override
	default FunctionType evaluate() {
		return this;
	}

	@Override
	default Epsilon contains(@NonNull Thing thing) {
		return thing.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Thing that) {
				return Nothing.TypeMiss(Void.this, that);
			}
			@Override
			public Epsilon handle(Nothing that) {
				return Epsilon.INSTANCE;
			}
		});
	}
	
	@Override
	public default Epsilon containsAll(Type type) {
		return type.accept(new Type.Visitor<Epsilon>(){
			@Override
			public Epsilon handle(Type that) {
				return Nothing.of(that.toString()+" is not empty");
			}
			@Override
			public Epsilon handle(Void that) {
				return Epsilon.INSTANCE;
			}
			@Override
			public Epsilon handle(Nothing that) {
				return Nothing.of(that.toString()+" is not a type");
			}			
		});
	}

	@Override
	public default Epsilon intersetcs(Type type) {
		return Nothing.of("no intersection");
	}

}
